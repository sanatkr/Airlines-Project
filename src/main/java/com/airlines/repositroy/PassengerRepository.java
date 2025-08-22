package com.airlines.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airlines.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {}