package com.n26.domain;

import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;

public class FinancialTransaction {

    private MonetaryAmount amount;

    private ZonedDateTime date;

    private FinancialTransaction(MonetaryAmount amount, ZonedDateTime date) {
	if (amount == null || amount.isNegativeOrZero()) {
	    throw new IllegalArgumentException("Transaction amount must be positive!");
	}

	if (date == null) {
	    throw new IllegalArgumentException("Transaction date cannot be null!");
	}

	this.amount = amount;
	this.date = date;
    }

    public static FinancialTransaction of(MonetaryAmount amount, ZonedDateTime date) {
	return new FinancialTransaction(amount, date);
    }

    public MonetaryAmount getAmount() {
	return amount;
    }

    public ZonedDateTime getDate() {
	return date;
    }

    public boolean isBefore(ZonedDateTime date) {
	return this.date.compareTo(date) < 0;
    }

    public boolean isAfter(ZonedDateTime date) {
	return this.date.compareTo(date) > 0;
    }

}
