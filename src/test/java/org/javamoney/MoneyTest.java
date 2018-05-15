package org.javamoney;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Test;

/**
 * These tests validate the behavior of the Java Money API
 */
public class MoneyTest {

    @Test
    public void shouldCreateEuroCurrency() {
	// given
	String code = "EUR";

	// when
	CurrencyUnit euro = Monetary.getCurrency(code);

	// then
	Assert.assertNotNull(euro);
	Assert.assertEquals(code, euro.getCurrencyCode());
	Assert.assertEquals(2, euro.getDefaultFractionDigits());
    }

    @Test
    public void shouldCreateMoneyFromDoubleValue() {
	// given
	double val = 20.15;
	CurrencyUnit euro = Monetary.getCurrency("EUR");

	// when
	Money money = Money.of(val, euro);

	// then
	Assert.assertEquals(euro, money.getCurrency());
	Assert.assertEquals("20.15", money.getNumber().toString());
    }

    @Test
    public void shouldNotRoundMoneyByDefault() {
	// given
	double val = 20.2055555555555;
	CurrencyUnit euro = Monetary.getCurrency("EUR");

	// when
	Money money = Money.of(val, euro);

	// then
	Assert.assertEquals(euro, money.getCurrency());
	Assert.assertEquals("20.2055555555555", money.getNumber().toString());
    }

    @Test
    public void shouldAddMoney() {
	// given
	CurrencyUnit euro = Monetary.getCurrency("EUR");
	Money money1 = Money.of(20.789, euro);
	Money money2 = Money.of(20.15, euro);

	// when
	Money result = money1.add(money2);

	// then
	Assert.assertEquals(euro, result.getCurrency());
	Assert.assertEquals("40.939", result.getNumber().toString());
    }

    @Test
    public void shouldSubtractMoney() {
	// given
	CurrencyUnit euro = Monetary.getCurrency("EUR");
	Money money1 = Money.of(20.789, euro);
	Money money2 = Money.of(20.15, euro);

	// when
	Money result = money1.subtract(money2);

	// then
	Assert.assertEquals(euro, result.getCurrency());
	Assert.assertEquals("0.639", result.getNumber().toString());
    }

}
