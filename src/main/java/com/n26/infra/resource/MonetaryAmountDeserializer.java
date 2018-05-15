package com.n26.infra.resource;

import java.io.IOException;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MonetaryAmountDeserializer extends StdDeserializer<MonetaryAmount> {

    private static final long serialVersionUID = 6593673265418485951L;

    // just for this code challenge,
    // let's assume that every amount has the same currency
    public static final String CURRENCY = "EUR";

    public MonetaryAmountDeserializer() {
	this(null);
    }

    public MonetaryAmountDeserializer(Class<?> clazz) {
	super(clazz);
    }

    @Override
    public MonetaryAmount deserialize(JsonParser parser, DeserializationContext ctx)
	    throws IOException, JsonProcessingException {
	double val = Double.valueOf(parser.getText());

	return Monetary.getDefaultAmountFactory().setCurrency(Monetary.getCurrency(CURRENCY)).setNumber(val).create();
    }

}
