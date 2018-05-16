package com.n26.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;

import org.junit.Assert;
import org.junit.Test;

import com.n26.helper.MoneyHelper;

public class StatisticTest {

    @Test
    public void shouldCreateStatisticFromTransaction() {
	// given
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(22.99);
	FinancialTransaction transaction = FinancialTransaction.of(amount, date);

	// when
	Statistic statistic = Statistic.newInstance(transaction);

	// then
	Assert.assertEquals(1, statistic.getCount());
	Assert.assertEquals(amount, statistic.getSum());
	Assert.assertEquals(amount, statistic.getAvg());
	Assert.assertEquals(amount, statistic.getMin());
	Assert.assertEquals(amount, statistic.getMax());
    }

    @Test
    public void shouldCreateStatisticWithoutTransaction() {
	// when
	Statistic statistic = Statistic.newInstance();

	// then
	Assert.assertEquals(0, statistic.getCount());
	Assert.assertEquals("0", statistic.getSum().getNumber().toString());
	Assert.assertEquals("0", statistic.getAvg().getNumber().toString());
	Assert.assertEquals("0", statistic.getMin().getNumber().toString());
	Assert.assertEquals("0", statistic.getMax().getNumber().toString());
    }

    @Test
    public void shouldConsolidateTransactionsCreatedOnSameDate() {
	// given
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	MonetaryAmount amount1 = MoneyHelper.newMonetaryAmount(22.99);
	FinancialTransaction transaction1 = FinancialTransaction.of(amount1, date);

	MonetaryAmount amount2 = MoneyHelper.newMonetaryAmount(13.17);
	FinancialTransaction transaction2 = FinancialTransaction.of(amount2, date);

	MonetaryAmount amount3 = MoneyHelper.newMonetaryAmount(32.82);
	FinancialTransaction transaction3 = FinancialTransaction.of(amount3, date);

	// when
	Statistic result = Statistic.newInstance(transaction1).consolidate(transaction2).consolidate(transaction3);

	// then
	Assert.assertEquals(3, result.getCount());
	Assert.assertEquals("68.98", result.getSum().getNumber().toString());
	Assert.assertEquals("22.99", result.getAvg().getNumber().toString());
	Assert.assertEquals("13.17", result.getMin().getNumber().toString());
	Assert.assertEquals("32.82", result.getMax().getNumber().toString());
    }

    @Test
    public void shouldNotConsolidateTransactionsCreatedOnDifferentDates() {
	// given
	ZonedDateTime date1 = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	MonetaryAmount amount1 = MoneyHelper.newMonetaryAmount(22.99);
	FinancialTransaction transaction1 = FinancialTransaction.of(amount1, date1);

	ZonedDateTime date2 = date1.plusMinutes(1);
	MonetaryAmount amount2 = MoneyHelper.newMonetaryAmount(13.17);
	FinancialTransaction transaction2 = FinancialTransaction.of(amount2, date2);

	// when
	Statistic result = Statistic.newInstance(transaction1).consolidate(transaction2);

	// then
	Assert.assertEquals(1, result.getCount());
	Assert.assertEquals("13.17", result.getSum().getNumber().toString());
	Assert.assertEquals("13.17", result.getAvg().getNumber().toString());
	Assert.assertEquals("13.17", result.getMin().getNumber().toString());
	Assert.assertEquals("13.17", result.getMax().getNumber().toString());
    }

}
