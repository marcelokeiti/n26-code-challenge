
package com.n26;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public final class MoneyParameter {

    private MoneyParameter() {
    };

    // just for this code challenge,
    // let's assume that every amount has the same currency
    public static final CurrencyUnit CURRENCY = Monetary.getCurrency("EUR");

}
