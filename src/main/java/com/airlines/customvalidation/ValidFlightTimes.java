package com.airlines.customvalidation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ArrivalAfterDepartureValidator.class)
@Target({ ElementType.TYPE })   // Apply at class level (because we need both fields)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFlightTimes {
	
	String message() default "Arrival time must be after departure time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
