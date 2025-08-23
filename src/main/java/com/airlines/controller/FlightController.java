package com.airlines.controller;

import java.time.LocalDate;



import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airlines.dto.FlightDTO;
import com.airlines.entity.Flight;
import com.airlines.service.FlightService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/flights")
public class FlightController {
	
		private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
		
		//org.apache.logging.log4j.Logger logger1 = LogManager.getLogger(FlightController.class);
	
		@Autowired
		private FlightService flightService;
		
		
		// Get all the flight details
		@GetMapping("/getDetails")
		public ResponseEntity<List<Flight>> getAllFlights() {
			
			//logger.info("Fetching all flights");
			
		    List<Flight> flights = flightService.getAllFlights();

		    if (flights.isEmpty()) {
		    	//logger.info("No Flights Found", flights.size());
		        return new ResponseEntity<List<Flight>>(HttpStatus.NO_CONTENT); 
		    }

		    //logger.info("Found {} flight(s)", flights.size());
		    return new ResponseEntity<List<Flight>>(flights, HttpStatus.OK); 
		}

	    
		// Add new flights to the database
	    @PostMapping("/addNew")
	    public ResponseEntity<List<Flight>> addFlight(@Valid @RequestBody List<FlightDTO> flights) {
	    	
	    	//logger1.info("started hiting the addNew api from controller");
	    	
	    	//logger.info("Received request to add {} flight(s)", flights.size());
	    	
	        List<Flight> savedFlights = flightService.addFlight(flights);
	        
	        //logger.info("Successfully added {} flight(s)", savedFlights.size());
	        
	        return new ResponseEntity<List<Flight>>(savedFlights, HttpStatus.CREATED);
	    }
	    
	    
	    // delete a particular flight
	    @DeleteMapping("/deleteFlight/{id}")
	    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
	    	
	    	logger.warn("Request to delete flight with ID {}", id);
	        boolean deleted = flightService.deleteFlight(id);
	        
	        if(deleted) 
	        {
	        	//String msg = "deleted successfully";
	        	logger.info("Flight with ID {} deleted successfully", id);
	            return new ResponseEntity<String>("deleted successfully",HttpStatus.OK); 
	        } 
	        else 
	        {
	        	logger.warn("No flight found with ID {} to delete", id);
	            return new ResponseEntity<String>("No record found",HttpStatus.NOT_FOUND); 
	        }
	    }
	    
	    // update a particular flight details with custom message
	    @PutMapping("/update/{id}")
	    public ResponseEntity<Map<String, Object>> updateFlight(@PathVariable("id") Long id, 
	    														@RequestBody Flight updatedFlight) {
	    	
	    	logger.info("Received request to update flight with ID {}", id);
	        logger.debug("Update payload: {}", updatedFlight);
	    	
	        Flight flight = flightService.updateFlight(id, updatedFlight);
	        
	        Map<String, Object> response = new HashMap<>();

	        if (flight != null) {
	        	
	        	logger.info("Flight with ID {} updated successfully", id);
	            response.put("message", "Flight updated successfully.");
	            response.put("flight", flight);
	            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	        } 
	        else {
	        	
	        	logger.warn("Flight with ID {} not found for update", id);
	            response.put("error", "Flight with ID " + id + " not found.");
	            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
	        }
	    }
	    
	    
	    // Get the flight details on a particular date
//	    @GetMapping("/by-date/{date}")								
//	    public ResponseEntity<List<Flight>> getFlightsByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//	        
//	    	logger.info("Received request to fetch flights for date: {}", date);
//	    	List<Flight> flights = flightService.getFlightsByDate(date);
//
//	        if (flights.isEmpty()) {
//	        	
//	        	logger.warn("No flights found for date: {}", date);
//	            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 if no flights
//	        } else {
//	        	
//	        	logger.info("Found {} flights for date: {}", flights.size(), date);
//	            return new ResponseEntity<>(flights, HttpStatus.OK); // 200 with data
//	        }
//	    }
	    
	    @GetMapping("/by-date/{date}")								
	    public ResponseEntity<?> getFlightsByDate(@PathVariable("date") 
	    										  //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	    										  @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
	        
	    	logger.info("Received request to fetch flights for date: {}", date);
	    	List<Flight> flights = flightService.getFlightsByDate(date);

	        if (flights.isEmpty()) {
	        	
	        	logger.warn("No flights found for date: {}", date);
	        	Map<String, Object> response = new HashMap<>();
	            response.put("message", "No flights present on this date");
	            response.put("status", HttpStatus.NOT_FOUND.value());
	            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND); // 204 if no flights
	        } else {
	        	
	        	logger.info("Found {} flights for date: {}", flights.size(), date);
	            return new ResponseEntity<>(flights, HttpStatus.OK); // 200 with data
	        }
	    }
	    
	    
	    // Get the flight details on a particular date between source and destination (using path variable)
	    @GetMapping("/search/{date}/{origin}/{destination}")
	    public ResponseEntity<?> getFlightsByDateAndRoute(
	            @PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
	            @PathVariable String origin,
	            @PathVariable String destination) {
	    	
	    	
	    	logger.info("Received request to search flights on {} from {} to {}", date, origin, destination);
	        List<Flight> flights = flightService.getFlightsByDateAndRoute(date, origin, destination);
	        
	        logger.debug("Query result: {} flight(s) found", flights.size());
	        if (flights.isEmpty()) {
	        	
	        	logger.warn("No flights found on {} from {} to {}", date, origin, destination);
	            Map<String, Object> response = new HashMap<>();
	            response.put("message", "No flights found for the given criteria.");
	            response.put("status", HttpStatus.OK);
	            
	            logger.info("Returning response with message: {}", response.get("message"));
	            return new ResponseEntity<>(response, HttpStatus.OK);
	            
	        } 
	        else {
	        	
	        	logger.info("Returning {} flight(s) for {} from {} to {}", flights.size(), date, origin, destination);
	            return new ResponseEntity<>(flights, HttpStatus.OK);
	        }
	        
//	        if (flights.isEmpty()) {
//	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	        } else {
//	            return new ResponseEntity<>(flights, HttpStatus.OK);
//	        }
	        
	    }
	    
	    
	    // getting the flights details on particular date, origin and destination with Global exception handler 
	    //(Using @RequestParam)
	    @GetMapping("/by-date-source-destination")
	    public ResponseEntity<List<Flight>> getFlights(
	            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
	            @RequestParam String source,
	            @RequestParam String destination) {

	    	logger.info("Received request to fetch flights on {} from {} to {}", date, source, destination);

	        List<Flight> flights = flightService.getFlightsByDateSourceDestination(date, source, destination);
	        
	        logger.debug("Query result: {} flight(s) found", flights.size());
	        
	        if (flights.isEmpty()) {
	            logger.warn("No flights found on {} from {} to {}", date, source, destination);
	        } else {
	            logger.info("Returning {} flight(s) for {} from {} to {}", flights.size(), date, source, destination);
	        }
	        return ResponseEntity.ok(flights);
	    }



	    //GET --> http://localhost:8080/api/flights/by-date-source-destination?date=15-08-2025&source=Delhi&destination=Mumbai



	    
//	    @GetMapping("/getDetails")
//	    public List<Flight> getAllFlights() {
//	        return flightService.getAllFlights();
//	    }

//	    @PostMapping("/addNew")
//	    public List<Flight> addFlight(@RequestBody List<Flight> flight) {
//	        return flightService.addFlight(flight);
//	    }

//	    @DeleteMapping("/{id}")
//	    public void deleteFlight(@PathVariable Long id) {
//	        flightService.deleteFlight(id);
//	    }
}
