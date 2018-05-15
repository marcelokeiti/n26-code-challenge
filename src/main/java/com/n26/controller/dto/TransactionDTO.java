package com.n26.controller.dto;

import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class TransactionDTO {

    @NotNull
    @PositiveOrZero
    public MonetaryAmount amount;

    @NotNull
    public ZonedDateTime timestamp;

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
