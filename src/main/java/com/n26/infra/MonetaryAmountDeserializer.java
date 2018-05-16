package com.n26.infra;

import java.io.IOException;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.n26.MoneyParameter;

public class MonetaryAmountDeserializer extends StdDeserializer<MonetaryAmount> {

    private static final long serialVersionUID = 6593673265418485951L;

    public MonetaryAmountDeserializer() {
	this(null);
    }

    public MonetaryAmountDeserializer(Class<?> clazz) {
	super(clazz);
    }

    @Override
    public MonetaryAmount deserialize(JsonParser parser, DeserializationContext ctx)
	    throws IOException, JsonProcessingException {
	double val = parser.getDoubleValue();

	return Monetary.getDefaultAmountFactory().setCurrency(MoneyParameter.CURRENCY).setNumber(val).create();
    }

}
