package com.airlines.repositroy;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.airlines.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	
	
	List<Flight> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
	
	
	List<Flight> findByDepartureTimeBetweenAndOriginIgnoreCaseAndDestinationIgnoreCase(
	        LocalDateTime start, LocalDateTime end, String origin, String destination);
	
	
	// Querying the DB using the JPQL (Java Persistence Query Language)
	@Query("SELECT f FROM Flight f WHERE " +
	           "f.origin = :source AND " +
	           "f.destination = :destination AND " +
	           "f.departureTime BETWEEN :startDateTime AND :endDateTime")
	    List<Flight> findFlightsByDateSourceDestination(
	            String source,
	            String destination,
	            LocalDateTime startDateTime,
	            LocalDateTime endDateTime
	    );


}
