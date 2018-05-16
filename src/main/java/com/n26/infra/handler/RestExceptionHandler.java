package com.n26.infra.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.n26.controller.dto.RestError;
import com.n26.exception.ExpiredTransactionException;

/**
 * This class is responsible to handle the response when an exception is not
 * treated.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
	    final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
	List<String> errors = new ArrayList<String>();
	for (FieldError error : ex.getBindingResult().getFieldErrors()) {
	    errors.add(error.getField() + ": " + error.getDefaultMessage());
	}
	for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
	    errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
	}

	RestError restError = new RestError(HttpStatus.BAD_REQUEST, errors);
	return handleExceptionInternal(ex, restError, headers, restError.getStatus(), request);
    }

    @ExceptionHandler({ ExpiredTransactionException.class })
    public ResponseEntity<Object> handleExpiredTransaction(final Exception ex, final WebRequest request) {
	return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleConflict(final Exception ex, final WebRequest request) {
	List<String> errors = new ArrayList<String>();
	errors.add(ex.getMessage());

	RestError restError = new RestError(HttpStatus.CONFLICT, errors);
	return new ResponseEntity<Object>(restError, new HttpHeaders(), restError.getStatus());
    }

}
