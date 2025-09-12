package com.airlines.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.airlines.entity.Passenger;
import com.airlines.repositroy.PassengerRepository;

@Service
public class PassengerLoginService implements UserDetailsService{
	
	@Autowired
	private PassengerRepository passengerRepository;

	@Override
	public UserDetails loadUserByUsername(String passengerEmail) throws UsernameNotFoundException {
		
		Passenger passenger = passengerRepository.findByEmail(passengerEmail);
		
		return User.builder()
                .username(passenger.getEmail())
                .password(passenger.getPwd())
                .roles(passenger.getRole()) // "USER" or "ADMIN"
                .build();
		
		//return new User(passenger.getEmail(), passenger.getPwd(), Collections.emptyList());

	}

	
	public boolean save(Passenger passenger) {
		
		Passenger savedPassenger = passengerRepository.save(passenger);
		
		if(savedPassenger!=null)
		{
			return true;
		}
		return false;
		
	}

}
