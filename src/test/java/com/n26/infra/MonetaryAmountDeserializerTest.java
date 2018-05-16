package com.n26.infra;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.JacksonConfiguration;
import com.n26.controller.dto.FinancialTransactionDTO;

public class MonetaryAmountDeserializerTest {

    ObjectMapper mapper = new JacksonConfiguration().objectMapper();

    @Test
    public void shoulDeserializeDouble() throws JsonParseException, JsonMappingException, IOException {
	// given
	String json = "{\"amount\": 20.89}";

	// when
	FinancialTransactionDTO transactionDTO = mapper.readValue(json, FinancialTransactionDTO.class);

	// then
	Assert.assertEquals("20.89", transactionDTO.getAmount().getNumber().toString());
    }

    @Test
    public void shoulDeserializeDoubleWithoutRounding() throws JsonParseException, JsonMappingException, IOException {
	// given
	String json = "{\"amount\": 20.8987978979}";

	// when
	FinancialTransactionDTO transactionDTO = mapper.readValue(json, FinancialTransactionDTO.class);

	// then
	Assert.assertEquals("20.8987978979", transactionDTO.getAmount().getNumber().toString());
    }

    @Test
    public void shoulDeserializeZeroDouble() throws JsonParseException, JsonMappingException, IOException {
	// given
	String json = "{\"amount\": 0}";

	// when
	FinancialTransactionDTO transactionDTO = mapper.readValue(json, FinancialTransactionDTO.class);

	// then
	Assert.assertEquals("0", transactionDTO.getAmount().getNumber().toString());
    }

    @Test
    public void shouldDeserializeNegativeDouble() throws JsonParseException, JsonMappingException, IOException {
	// given
	String json = "{\"amount\": -20.89}";

	// when
	FinancialTransactionDTO transactionDTO = mapper.readValue(json, FinancialTransactionDTO.class);

	// then
	Assert.assertEquals("-20.89", transactionDTO.getAmount().getNumber().toString());
    }

    @Test(expected = JsonParseException.class)
    public void shoulNotDeserializeMalFormatDouble() throws JsonParseException, JsonMappingException, IOException {
	// given
	String json = "{\"amount\": 20,89}";

	// when
	mapper.readValue(json, FinancialTransactionDTO.class);

	// then
	Assert.fail();
    }

}
