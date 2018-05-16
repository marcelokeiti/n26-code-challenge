package com.n26.controller;

public class TransactionDTO {

    private double amount;

    private long timestamp;

    public TransactionDTO() {

    }

    public TransactionDTO(double amount, long timestamp) {
	super();
	this.amount = amount;
	this.timestamp = timestamp;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public long getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
    }

}
