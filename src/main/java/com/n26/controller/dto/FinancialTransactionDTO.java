package com.n26.controller.dto;

import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.n26.controller.validador.NotFuture;

public class FinancialTransactionDTO {

    @NotNull
    @Positive
    private MonetaryAmount amount;

    @NotNull
    @NotFuture
    private ZonedDateTime timestamp;

    public MonetaryAmount getAmount() {
	return amount;
    }

    public void setAmount(MonetaryAmount amount) {
	this.amount = amount;
    }

    public ZonedDateTime getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
	this.timestamp = timestamp;
    }

    @Override
    public String toString() {
	return "TransactionDTO [amount=" + amount + ", timestamp=" + timestamp + "]";
    }

}
