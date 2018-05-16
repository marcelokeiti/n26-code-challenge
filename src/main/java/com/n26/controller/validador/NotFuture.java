package com.n26.controller.validador;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation is used to ensure that the date is not in the future
 */
@Documented
@Constraint(validatedBy = NotFutureValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFuture {

    String message() default "Timestamp cannot be in the future.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
