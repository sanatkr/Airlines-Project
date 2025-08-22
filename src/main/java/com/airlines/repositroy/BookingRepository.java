package com.airlines.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airlines.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
