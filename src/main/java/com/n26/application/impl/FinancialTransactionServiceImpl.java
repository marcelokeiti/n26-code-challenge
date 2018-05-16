package com.n26.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.application.FinancialTransactionService;
import com.n26.application.StatisticService;
import com.n26.domain.FinancialTransaction;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    @Autowired
    private StatisticService statisticsService;

    @Override
    public void create(FinancialTransaction transaction) {
	statisticsService.add(transaction);
    }

}
