package com.n26.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;

import org.junit.Assert;
import org.junit.Test;

import com.n26.helper.MoneyHelper;

public class FinancialTransactionTest {

    @Test
    public void shouldCreateFinancialTransaction() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	// when
	FinancialTransaction transaction = FinancialTransaction.of(amount, date);

	// then
	Assert.assertEquals(amount, transaction.getAmount());
	Assert.assertEquals(date, transaction.getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateFinancialTransactionWithNegativeAmount() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(-35.45);
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	// when
	FinancialTransaction.of(amount, date);

	// then
	Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateFinancialTransactionWithZeroAmount() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(0);
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	// when
	FinancialTransaction.of(amount, date);

	// then
	Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateFinancialTransactionWithoutAmount() {
	// given
	MonetaryAmount amount = null;
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);

	// when
	FinancialTransaction.of(amount, date);

	// then
	Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateFinancialTransactionWithoutDate() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	ZonedDateTime date = null;

	// when
	FinancialTransaction.of(amount, date);

	// then
	Assert.fail();
    }

    @Test
    public void shouldCheckIfDateIsAfterTransactionDate() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	FinancialTransaction transaction = FinancialTransaction.of(amount, date);

	// then
	Assert.assertTrue(transaction.isAfter(date.minusSeconds(1)));
	Assert.assertFalse(transaction.isAfter(date.plusSeconds(1)));
    }

    @Test
    public void shouldCheckIfDateIsBeforeTransactionDate() {
	// given
	MonetaryAmount amount = MoneyHelper.newMonetaryAmount(35.45);
	ZonedDateTime date = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	FinancialTransaction transaction = FinancialTransaction.of(amount, date);

	// then
	Assert.assertTrue(transaction.isBefore(date.plusSeconds(1)));
	Assert.assertFalse(transaction.isBefore(date.minusSeconds(1)));
    }

}
