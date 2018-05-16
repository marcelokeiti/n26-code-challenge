package com.n26.application;

import com.n26.domain.FinancialTransaction;

public interface FinancialTransactionService {

    void create(FinancialTransaction transaction);

}
