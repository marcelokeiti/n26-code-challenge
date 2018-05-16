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

    private MonetaryAmount max;

    private MonetaryAmount min;

    private long count;

    private ZonedDateTime transactionDate;

    private Statistic() {
	this.count = 0;
	this.sum = ZERO_AMOUNT;
	this.min = ZERO_AMOUNT;
	this.max = ZERO_AMOUNT;
	this.transactionDate = ZonedDateTime.of(LocalDateTime.MIN, ZoneOffset.UTC);
    }

    private Statistic(FinancialTransaction transaction) {
	if (transaction == null) {
	    throw new IllegalStateException("Transaction cannot be null!");
	}

	this.init(transaction);
    }

    private void init(FinancialTransaction transaction) {
	MonetaryAmount amount = transaction.getAmount();

	this.count = 1;
	this.sum = amount;
	this.min = amount;
	this.max = amount;
	this.transactionDate = transaction.getDate();
    }

    public static Statistic newInstance() {
	return new Statistic();
    }

    public static Statistic newInstance(FinancialTransaction transaction) {
	return new Statistic(transaction);
    }

    /**
     * This method consolidates the transaction amount with same transaction date.
     * 
     * @param transaction
     * @return
     */
    public Statistic consolidate(FinancialTransaction transaction) {

	if (this.hasSameTransactionDateThan(transaction)) {
	    MonetaryAmount amount = transaction.getAmount();

	    this.count += 1;
	    this.sum = this.sum.add(amount);

	    if (min.equals(ZERO_AMOUNT) || amount.compareTo(this.min) < 0) {
		this.min = amount;
	    }

	    if (max.equals(ZERO_AMOUNT) || amount.compareTo(this.max) > 0) {
		this.max = amount;
	    }

	    this.transactionDate = transaction.getDate();
	} else {
	    this.init(transaction);
	}

	return this;
    }

    public Statistic consolidate(Statistic statistic) {
	this.count += statistic.count;

	this.sum = this.sum.add(statistic.sum);

	if (min.equals(ZERO_AMOUNT) || statistic.min.compareTo(this.min) < 0) {
	    this.min = statistic.min;
	}

	if (max.equals(ZERO_AMOUNT) || statistic.max.compareTo(this.max) > 0) {
	    this.max = statistic.max;
	}

	return this;
    }

    private boolean hasSameTransactionDateThan(FinancialTransaction transaction) {
	return transactionDate.isEqual(transaction.getDate());
    }

    public boolean isValidOnDate(ZonedDateTime date) {
	return date.minusSeconds(59).compareTo(transactionDate) <= 0;
    }

    public MonetaryAmount getSum() {
	return sum;
    }

    public MonetaryAmount getAvg() {
	return count == 0 ? ZERO_AMOUNT : sum.divide(count).with(Monetary.getDefaultRounding());
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
