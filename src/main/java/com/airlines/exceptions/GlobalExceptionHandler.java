package com.airlines.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// Handle custom AirlineException
    @ExceptionHandler(AirlineException.class)
    public ResponseEntity<Map<String, Object>> handleAirlineException(AirlineException ex) {
    	
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    
 // Handle HandlerMethodValidationException validation errors
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError) 
                                ? ((FieldError) error).getField() 
                                : (error.getCodes() != null && error.getCodes().length > 0 ? error.getCodes()[0] : "unknown");
            errors.put(fieldName, error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(HandlerMethodValidationException.class)
//    public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getAllErrors().forEach(error -> {
//            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
//            errors.put(fieldName, error.getDefaultMessage());
//        });
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
    
    
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    
    
 // Handle all other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Something went wrong, please try again later.");
        response.put("details", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    


}
