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
import com.n26.controller.dto.StatisticDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StatisticControllerTest {

    @Autowired
    private FakeTimeProvider timeProvider;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetStatisticConsolidatedInTheLastMinute() {
	// given
	ZonedDateTime now = ZonedDateTime.of(2018, 03, 15, 10, 30, 0, 0, ZoneOffset.UTC);
	timeProvider.setTime(now);

	for (int i = 0; i < 60; i++) {
	    if (i < 3) {
		// discarded
		addTransaction(999.99, now.minusSeconds(59 - i));
	    }
	    if (i == 35) {
		// max
		addTransaction(9.12, now.minusSeconds(59 - i));
	    } else if (i == 45) {
		// min
		addTransaction(0.99, now.minusSeconds(59 - i));
	    } else {
		addTransaction(1.11, now.minusSeconds(59 - i));
	    }
	}

	now = now.plusSeconds(3); // lapse 3 secs
	timeProvider.setTime(now);

	// when
	ResponseEntity<StatisticDTO> response = restTemplate.getForEntity("/statistics", StatisticDTO.class);
	StatisticDTO statistic = response.getBody();

	// then
	Assert.assertEquals(57, statistic.getCount());
	Assert.assertEquals("71.16", String.valueOf(statistic.getSum()));
	Assert.assertEquals("1.25", String.valueOf(statistic.getAvg())); // (1.11 x 55 + 9.12 + 0.99) / 57
	Assert.assertEquals("0.99", String.valueOf(statistic.getMin()));
	Assert.assertEquals("9.12", String.valueOf(statistic.getMax()));
    }

    private void addTransaction(double val, ZonedDateTime date) {
	TransactionDTO transactionDTO = new TransactionDTO(val, date.toInstant().toEpochMilli());
	ResponseEntity<String> response = restTemplate.postForEntity("/transactions", transactionDTO, String.class);

	Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
    }

}
