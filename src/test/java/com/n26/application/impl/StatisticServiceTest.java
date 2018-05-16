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

	MonetaryAmount amount1 = MoneyHelper.newMonetaryAmount(22.99);
	FinancialTransaction transaction1 = FinancialTransaction.of(amount1, transactionDate);

	MonetaryAmount amount2 = MoneyHelper.newMonetaryAmount(13.17);
	FinancialTransaction transaction2 = FinancialTransaction.of(amount2, transactionDate);

	MonetaryAmount amount3 = MoneyHelper.newMonetaryAmount(32.82);
	FinancialTransaction transaction3 = FinancialTransaction.of(amount3, transactionDate);

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

	ZonedDateTime transactionDate = now.minusSeconds(35);
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(22.99);
	FinancialTransaction transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

	statisticService.add(transaction);

	// and
	now = now.plusMinutes(1);

	transactionDate = transactionDate.plusMinutes(1);
	amount = MoneyHelper.newMonetaryAmount(13.17);
	transaction = FinancialTransaction.of(amount, transactionDate);

	Mockito.when(timeProvider.now()).thenReturn(now);

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

}
