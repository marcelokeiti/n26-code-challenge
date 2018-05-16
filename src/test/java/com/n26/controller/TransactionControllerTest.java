package com.n26.controller;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.n26.Application;
import com.n26.application.impl.FakeTimeProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionControllerTest {

    @Autowired
    private FakeTimeProvider timeProvider;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldAddTransactionCreatedDuringTheLastMinute() throws Exception {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusSeconds(35);

	TransactionDTO transactionDTO = new TransactionDTO(35.45, transactionDate.toInstant().toEpochMilli());

	timeProvider.setTime(now);

	// when
	ResponseEntity<String> response = restTemplate.postForEntity("/transactions", transactionDTO, String.class);

	// then
	Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
    }

    @Test
    public void shouldNotAddTransactionCreatedBefore59Seconds() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.minusMinutes(1);

	TransactionDTO transactionDTO = new TransactionDTO(35.45, transactionDate.toInstant().toEpochMilli());

	timeProvider.setTime(now);

	// when
	ResponseEntity<String> response = restTemplate.postForEntity("/transactions", transactionDTO, String.class);

	// then
	Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    }

    @Test
    public void shouldNotAddTransactionCreatedAfterNow() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 01, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	ZonedDateTime transactionDate = now.plusSeconds(1);

	TransactionDTO transactionDTO = new TransactionDTO(35.45, transactionDate.toInstant().toEpochMilli());

	timeProvider.setTime(now);

	// when
	ResponseEntity<String> response = restTemplate.postForEntity("/transactions", transactionDTO, String.class);

	// then
	Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

}
