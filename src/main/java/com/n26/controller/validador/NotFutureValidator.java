package com.n26.controller.validador;

import java.time.ZonedDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.application.TimeProvider;

@Component
public class NotFutureValidator implements ConstraintValidator<NotFuture, ZonedDateTime> {

    @Autowired
    private TimeProvider timeProvider;

    @Override
    public void initialize(final NotFuture constraint) {

    }

    @Override
    public boolean isValid(final ZonedDateTime date, final ConstraintValidatorContext ctx) {
	ZonedDateTime now = timeProvider.now();

	return date.compareTo(now) <= 0;
    }

}
