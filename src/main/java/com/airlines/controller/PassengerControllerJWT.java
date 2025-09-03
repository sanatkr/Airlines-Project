package com.airlines.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airlines.entity.Passenger;
import com.airlines.service.JWTService;
import com.airlines.service.PassengerLoginService;

@RestController
@RequestMapping("/api/flights/jwt")
public class PassengerControllerJWT {
	
	@Autowired
	private PassengerLoginService passengerLoginSerivce;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@GetMapping("/check")
	public String checkAfterLogin()
	{
		return "Welcome to the Airlines Application";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginCheck(@RequestBody Passenger passenger) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(passenger.getEmail(),
				passenger.getPwd());

		try 
		{
			Authentication authenticate = authenticationManager.authenticate(token);
			
			if (authenticate.isAuthenticated()) 
			{
				String jwtToken = jwtService.generateToken(passenger.getEmail());
				return new ResponseEntity<>(jwtToken, HttpStatus.OK);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> saveCustomer(@RequestBody Passenger passenger) {
		
		String encodedPwd = passwordEncoder.encode(passenger.getPwd());
		passenger.setPwd(encodedPwd);

		boolean status = passengerLoginSerivce.save(passenger);
		
		if(status)
		{
			return new ResponseEntity<>("Customer Registered", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Customer Not Registered", HttpStatus.INTERNAL_SERVER_ERROR);

	}

	
	
}
