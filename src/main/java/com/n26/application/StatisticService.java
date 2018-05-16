package com.n26.application;

import com.n26.domain.FinancialTransaction;
import com.n26.domain.Statistic;

public interface StatisticService {

    void add(FinancialTransaction transaction);

    Statistic get();

}
