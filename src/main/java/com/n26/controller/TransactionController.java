package com.n26.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.controller.dto.TransactionDTO;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody final TransactionDTO transactionDTO) {
	LOGGER.info("Received: {}", transactionDTO);

	return ResponseEntity.noContent().build();
    }

}
