package com.n26.application.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
import com.n26.exception.ExpiredTransactionException;
import com.n26.helper.MoneyHelper;

@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceTest {

    @Mock
    private TimeProvider timeProvider;

    private StatisticServiceImpl statisticService;

    @Before
    public void test() {
	statisticService = new StatisticServiceImpl(timeProvider);
	statisticService.initialize();
    }

    @Test(expected = ExpiredTransactionException.class)
    public void shouldNotAddTransactionCreatedBefore59Seconds() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusMinutes(1);

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction);

	// then
	Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAddTransactionCreatedAfterNow() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.plusSeconds(1);

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction);

	// then
	Assert.fail();
    }

    @Test
    public void shouldAddTransactionCreated59SecondsAgo() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusSeconds(59);

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction);

	// then
	Statistic statistic = statisticService.minuteStatistic[transactionDate.getSecond()];
	Assert.assertEquals(1, statistic.getCount());
	Assert.assertEquals(amount, statistic.getMin());
	Assert.assertEquals(amount, statistic.getMax());
	Assert.assertEquals(amount, statistic.getSum());
	Assert.assertEquals(amount, statistic.getAvg());
    }

    @Test
    public void shouldAddTransactionCreatedNow() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now;

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction);

	// then
	Statistic statistic = statisticService.minuteStatistic[transactionDate.getSecond()];
	Assert.assertEquals(1, statistic.getCount());
	Assert.assertEquals(amount, statistic.getMin());
	Assert.assertEquals(amount, statistic.getMax());
	Assert.assertEquals(amount, statistic.getSum());
	Assert.assertEquals(amount, statistic.getAvg());
    }

    @Test
    public void shouldAddTransactionCreatedDuringTheLastMinute() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusSeconds(35);

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction);

	// then
	Statistic statistic = statisticService.minuteStatistic[transactionDate.getSecond()];
	Assert.assertEquals(1, statistic.getCount());
	Assert.assertEquals(amount, statistic.getMin());
	Assert.assertEquals(amount, statistic.getMax());
	Assert.assertEquals(amount, statistic.getSum());
	Assert.assertEquals(amount, statistic.getAvg());
    }

    @Test
    public void shouldConsolidateTransactionsCreatedOnSameDate() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusSeconds(35);

	FinancialTransaction transaction1 = buildTransaction(22.99, transactionDate);
	FinancialTransaction transaction2 = buildTransaction(13.17, transactionDate);
	FinancialTransaction transaction3 = buildTransaction(32.82, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	statisticService.add(transaction1);
	statisticService.add(transaction2);
	statisticService.add(transaction3);

	// then
	Statistic statistic = statisticService.minuteStatistic[transactionDate.getSecond()];
	Assert.assertEquals(3, statistic.getCount());
	Assert.assertEquals("68.98", statistic.getSum().getNumber().toString());
	Assert.assertEquals("22.99", statistic.getAvg().getNumber().toString());
	Assert.assertEquals("13.17", statistic.getMin().getNumber().toString());
	Assert.assertEquals("32.82", statistic.getMax().getNumber().toString());
    }

    @Test
    public void shouldNotConsolidateTransactionsCreatedOnDifferentDates() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	Mockito.when(timeProvider.now()).thenReturn(now);

	ZonedDateTime transactionDate = now.minusSeconds(35);
	FinancialTransaction transaction = buildTransaction(22.99, transactionDate);

	statisticService.add(transaction);

	// and
	now = now.plusMinutes(1);
	Mockito.when(timeProvider.now()).thenReturn(now);

	transactionDate = transactionDate.plusMinutes(1);
	transaction = buildTransaction(13.17, transactionDate);

	// when
	statisticService.add(transaction);

	// then
	Statistic statistic = statisticService.minuteStatistic[transactionDate.getSecond()];
	Assert.assertEquals(1, statistic.getCount());
	Assert.assertEquals("13.17", statistic.getSum().getNumber().toString());
	Assert.assertEquals("13.17", statistic.getAvg().getNumber().toString());
	Assert.assertEquals("13.17", statistic.getMin().getNumber().toString());
	Assert.assertEquals("13.17", statistic.getMax().getNumber().toString());
    }

    @Test
    public void shouldGetStatisticConsolidatedInTheLastMinute() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	Mockito.when(timeProvider.now()).thenReturn(now);

	for (int i = 0; i < 60; i++) {
	    if (i < 3) {
		// discarded
		addTransaction(999.99, now.minusSeconds(59 - i));
	    }
	    if (i == 35) {
		// max
		addTransaction(9.12, now.minusSeconds(59 - i));
	    } else if (i == 45) {
		// min
		addTransaction(0.99, now.minusSeconds(59 - i));
	    } else {
		addTransaction(1.11, now.minusSeconds(59 - i));
	    }
	}

	now = now.plusSeconds(3); // lapse 3 secs
	Mockito.when(timeProvider.now()).thenReturn(now);

	// when
	Statistic statistic = statisticService.get();

	// then
	Assert.assertEquals(57, statistic.getCount());
	Assert.assertEquals("71.16", statistic.getSum().getNumber().toString());
	Assert.assertEquals("1.25", statistic.getAvg().getNumber().toString()); // (1.11 x 55 + 9.12 + 0.99) / 57
	Assert.assertEquals("0.99", statistic.getMin().getNumber().toString());
	Assert.assertEquals("9.12", statistic.getMax().getNumber().toString());
    }

    private void addTransaction(double val, ZonedDateTime date) {
	FinancialTransaction transaction = buildTransaction(val, date);
	statisticService.add(transaction);
    }

    private FinancialTransaction buildTransaction(double val, ZonedDateTime date) {
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(val);
	return FinancialTransaction.of(amount, date);
    }

}
