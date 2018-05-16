package com.n26.application.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.money.MonetaryAmount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.application.TimeProvider;
import com.n26.domain.FinancialTransaction;
import com.n26.domain.Statistic;
import com.n26.helper.MoneyHelper;

@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceConcurrentTest {

    @Mock
    private TimeProvider timeProvider;

    private StatisticServiceImpl statisticService;

    // latch used to control threads execution
    private CountDownLatch readyThreadCounter;
    private CountDownLatch callingThreadBlocker;
    private CountDownLatch completedThreadCounter;

    @Before
    public void test() {
	statisticService = new StatisticServiceImpl(timeProvider);
	statisticService.initialize();

	readyThreadCounter = new CountDownLatch(63);
	callingThreadBlocker = new CountDownLatch(1);
	completedThreadCounter = new CountDownLatch(63);
    }

    @Test
    public void shouldAddTransactionConcurrently() throws InterruptedException {
	// given
	List<Thread> workers = new ArrayList<>();

	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	Mockito.when(timeProvider.now()).thenReturn(now);

	for (int i = 0; i < 60; i++) {
	    if (i < 3) {
		// discarded
		workers.add(buildRegisterTransactionThread(999.99, now.minusSeconds(59 - i)));
	    }

	    if (i == 35) {
		// max
		workers.add(buildRegisterTransactionThread(9.12, now.minusSeconds(59 - i)));
	    } else if (i == 45) {
		// min
		workers.add(buildRegisterTransactionThread(0.99, now.minusSeconds(59 - i)));
	    } else {
		workers.add(buildRegisterTransactionThread(1.11, now.minusSeconds(59 - i)));
	    }
	}

	now = now.plusSeconds(3); // lapse 3 secs
	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	workers.forEach(Thread::start);

	readyThreadCounter.await();
	callingThreadBlocker.countDown();
	completedThreadCounter.await();

	// then
	Statistic statistic = statisticService.get();

	Assert.assertEquals(57, statistic.getCount());
	Assert.assertEquals("71.16", statistic.getSum().getNumber().toString());
	Assert.assertEquals("1.25", statistic.getAvg().getNumber().toString()); // (1.11 x 55 + 9.12 + 0.99) / 57
	Assert.assertEquals("0.99", statistic.getMin().getNumber().toString());
	Assert.assertEquals("9.12", statistic.getMax().getNumber().toString());
    }

    private Thread buildRegisterTransactionThread(double val, ZonedDateTime date) {
	FinancialTransaction transaction = buildTransaction(val, date);
	return new Thread(new RegisterTransactionWorker(statisticService, transaction, readyThreadCounter,
		callingThreadBlocker, completedThreadCounter));
    }

    private FinancialTransaction buildTransaction(double val, ZonedDateTime date) {
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(val);
	return FinancialTransaction.of(amount, date);
    }

}
