package com.n26.application.impl;

import java.util.concurrent.CountDownLatch;

import com.n26.application.StatisticService;
import com.n26.domain.FinancialTransaction;

public class RegisterTransactionWorker implements Runnable {

    private StatisticService statisticService;
    private FinancialTransaction transaction;

    private CountDownLatch readyThreadCounter;
    private CountDownLatch callingThreadBlocker;
    private CountDownLatch completedThreadCounter;

    public RegisterTransactionWorker(StatisticService statisticService, FinancialTransaction transaction,
	    CountDownLatch readyThreadCounter, CountDownLatch callingThreadBlocker,
	    CountDownLatch completedThreadCounter) {

	this.statisticService = statisticService;
	this.transaction = transaction;

	this.readyThreadCounter = readyThreadCounter;
	this.callingThreadBlocker = callingThreadBlocker;
	this.completedThreadCounter = completedThreadCounter;
    }

    @Override
    public void run() {
	readyThreadCounter.countDown();
	try {
	    callingThreadBlocker.await();
	    statisticService.add(transaction);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} finally {
	    completedThreadCounter.countDown();
	}
    }

}
