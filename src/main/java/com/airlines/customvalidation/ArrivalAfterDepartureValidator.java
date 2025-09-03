package com.airlines.customvalidation;

import com.airlines.dto.FlightDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ArrivalAfterDepartureValidator implements ConstraintValidator<ValidFlightTimes, FlightDTO> {

	@Override
	public boolean isValid(FlightDTO flight, ConstraintValidatorContext context) {
		
		 if (flight.getDepartureTime() == null || flight.getArrivalTime() == null) {
	            return true; // let @NotNull handle null case
	        }
	        return flight.getArrivalTime().isAfter(flight.getDepartureTime());
	        
	}

	
}
