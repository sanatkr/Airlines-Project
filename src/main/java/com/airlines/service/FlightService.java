package com.airlines.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airlines.dto.FlightDTO;
import com.airlines.entity.Flight;
import com.airlines.exceptions.AirlineException;
import com.airlines.repositroy.FlightRepository;


@Service
public class FlightService {
	
	//private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
	

	
	@Autowired
	private FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    
    public List<Flight> addFlight(List<FlightDTO> flightDTO) {
        
    	
    	List<Flight> flights = new ArrayList<>();
    	
    	for(FlightDTO dto:flightDTO)
    	{
    		flights.add(this.dtoToEntity(dto));
    	}

        return flightRepository.saveAll(flights);
    }

    
    public boolean deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    public Flight updateFlight(Long id, Flight updatedFlight) {
    	
    	Optional<Flight> existingFlight = flightRepository.findById(id);
    	
    	if(existingFlight.isPresent())
    	{
    		existingFlight.get().setFlightNumber(updatedFlight.getFlightNumber());
            existingFlight.get().setOrigin(updatedFlight.getOrigin());
            existingFlight.get().setDestination(updatedFlight.getDestination());
            existingFlight.get().setDepartureTime(updatedFlight.getDepartureTime());
            existingFlight.get().setArrivalTime(updatedFlight.getArrivalTime());
            existingFlight.get().setAvailableSeats(updatedFlight.getAvailableSeats());
            return flightRepository.save(existingFlight.get());
    	}
    	else
    	{
    		return null;
    	}
        
            
    }
    
    
    public List<Flight> getFlightsByDate(LocalDate date) {
    	
    	//logger.info("Service: Fetching flights for date: {}", date);
    	
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        //logger.debug("Service: Date range computed - Start: {}, End: {}", startOfDay, endOfDay);
        
        List<Flight> flights = flightRepository.findByDepartureTimeBetween(startOfDay, endOfDay);

        if (flights.isEmpty()) {
            //logger.warn("Service: No flights found between {} and {}", startOfDay, endOfDay);
        } else {
            //logger.info("Service: Found {} flights between {} and {}", flights.size(), startOfDay, endOfDay);
        }

        return flights;
    }

    
    
    public List<Flight> getFlightsByDateAndRoute(LocalDate date, String origin, String destination) {
    	
    	//logger.info("Service: Fetching flights for date: {}, origin: {}, destination: {}", date, origin, destination);
    	
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        //logger.debug("Service: Computed date range - Start: {}, End: {}", startOfDay, endOfDay);

        List<Flight> flights = flightRepository
                .findByDepartureTimeBetweenAndOriginIgnoreCaseAndDestinationIgnoreCase(
                        startOfDay, endOfDay, origin, destination);

        if (flights.isEmpty()) {
        	
            //logger.warn("Service: No flights found for date: {}, origin: {}, destination: {}", date, origin, destination);
        } 
        else {
        	
            //logger.info("Service: Found {} flights for date: {}, origin: {}, destination: {}", flights.size(), date, origin, destination);
        }

        return flights;
    }
    
    
    // getting the flights details on particular date, origin and destination with Global exception handler
    public List<Flight> getFlightsByDateSourceDestination(LocalDate date, String source, String destination) {
    	
    	//logger.info("Service: Fetching flights for date: {}, source: {}, destination: {}", date, source, destination);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        //logger.debug("Service: Computed date range - Start: {}, End: {}", startOfDay, endOfDay);

        List<Flight> flights = flightRepository.findFlightsByDateSourceDestination(
                source,
                destination,
                startOfDay,
                endOfDay
        );

        if (flights.isEmpty()) {
        	
            //logger.warn("Service: No flights found from {} to {} on {}", source, destination, date);
            throw new AirlineException("No flights found from " + source + " to " + destination + " on " + date);
        }

        //logger.info("Service: Found {} flights from {} to {} on {}", flights.size(), source, destination, date);
        
        return flights;
    }
    
    public Flight dtoToEntity(FlightDTO dto)
    {
    	Flight flight = new Flight();
    	
    	flight.setFlightNumber(dto.getFlightNumber());
    	flight.setOrigin(dto.getOrigin());
    	flight.setDestination(dto.getDestination());
    	flight.setDepartureTime(dto.getDepartureTime());
    	flight.setArrivalTime(dto.getArrivalTime());
    	flight.setAvailableSeats(dto.getAvailableSeats());
    	
    	return flight;
    }


//    public void deleteFlight(Long id) {
//        flightRepository.deleteById(id);
//    }

}
