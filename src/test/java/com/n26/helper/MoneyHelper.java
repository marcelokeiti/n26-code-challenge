package com.n26.helper;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.n26.MoneyParameter;

public class MoneyHelper {

    public static MonetaryAmount newMonetaryAmount(double val) {
	return Monetary.getDefaultAmountFactory().setCurrency(MoneyParameter.CURRENCY).setNumber(val).create();
    }

}
