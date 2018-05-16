package com.n26.domain;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.n26.MoneyParameter;

/**
 * <p>
 * This class represents the statistic collected in a specific date and time.
 * </p>
 */
public class Statistic {

    private static final MonetaryAmount ZERO_AMOUNT = Monetary.getDefaultAmountFactory()
	    .setCurrency(MoneyParameter.CURRENCY).setNumber(0).create();

    private MonetaryAmount sum;

    private MonetaryAmount avg;

    private MonetaryAmount max;

    private MonetaryAmount min;

    private long count;

    private ZonedDateTime transactionDate;

    private Statistic() {
	reset();
    }

    private Statistic(FinancialTransaction transaction) {
	this();

	if (transaction != null) {
	    this.consolidate(transaction);
	}
    }

    private void reset() {
	this.sum = ZERO_AMOUNT;
	this.avg = ZERO_AMOUNT;
	this.max = ZERO_AMOUNT;
	this.min = ZERO_AMOUNT;
	this.count = 0l;

	this.transactionDate = ZonedDateTime.of(LocalDateTime.MIN, ZoneOffset.UTC);
    }

    public static Statistic newInstance() {
	return new Statistic();
    }

    public static Statistic newInstance(FinancialTransaction transaction) {
	return new Statistic(transaction);
    }

    public Statistic consolidate(FinancialTransaction transaction) {
	if (!transactionDate.isEqual(transaction.getDate())) {
	    this.reset();
	}

	MonetaryAmount amount = transaction.getAmount();

	this.count++;
	this.sum = this.sum.add(amount);
	this.avg = sum.divide(this.count).with(Monetary.getDefaultRounding());

	if (min.equals(ZERO_AMOUNT) || amount.compareTo(this.min) < 0) {
	    this.min = amount;
	}

	if (max.equals(ZERO_AMOUNT) || amount.compareTo(this.max) > 0) {
	    this.max = amount;
	}

	this.transactionDate = transaction.getDate();
	return this;
    }

    public MonetaryAmount getSum() {
	return sum;
    }

    public MonetaryAmount getAvg() {
	return avg;
    }

    public MonetaryAmount getMax() {
	return max;
    }

    public MonetaryAmount getMin() {
	return min;
    }

    public long getCount() {
	return count;
    }

}
