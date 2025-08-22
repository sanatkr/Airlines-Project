package com.airlines.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FlightControllerAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(FlightControllerAspect.class);
	
	@Around("execution(* com.airlines.controller.FlightController.addFlight(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method arguments
        Object[] args = joinPoint.getArgs();

        // Assuming the first argument is List<Flight>
        if (args != null && args.length > 0 && args[0] instanceof List) {
            List<?> flights = (List<?>) args[0];
            logger.info("FlightControllerAspect : Received request to add {} flight(s)", flights.size());
        }

        // Proceed with the method execution (addFlight() method of the FlightController)
        
        Object result = joinPoint.proceed();

        // Extract the saved flights from the method result (returned value from the addFlight() method of the FlightController class)
        if (result instanceof org.springframework.http.ResponseEntity) {
            org.springframework.http.ResponseEntity<?> responseEntity = (org.springframework.http.ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            if (body instanceof List) {
                List<?> savedFlights = (List<?>) body;
                logger.info("FlightControllerAspect : Successfully added {} flight(s)", savedFlights.size());
                
            }
        }

        return result;
    }
	
	

}
