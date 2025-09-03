package com.airlines.dto;

import java.time.LocalDateTime;

import com.airlines.customvalidation.ValidFlightTimes;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@ValidFlightTimes
public class FlightDTO {
    
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Future(message = "Arrival time must be in the future")
    private LocalDateTime arrivalTime;

    @Min(value = 1, message = "At least one seat must be available")
    private int availableSeats;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
    
    

}
