package com.n26.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.application.FinancialTransactionService;
import com.n26.controller.dto.FinancialTransactionDTO;
import com.n26.domain.FinancialTransaction;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private FinancialTransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody final FinancialTransactionDTO transactionDTO) {
	LOGGER.info("Received: {}", transactionDTO);

	FinancialTransaction transaction = FinancialTransaction.of(transactionDTO.getAmount(),
		transactionDTO.getTimestamp());

	transactionService.create(transaction);

	return ResponseEntity.created(null).build();
    }

}
