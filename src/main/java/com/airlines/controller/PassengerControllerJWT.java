package com.airlines.controller;

import java.util.HashMap;

import java.util.Map;

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
import com.airlines.repositroy.PassengerRepository;
import com.airlines.service.JWTService;
import com.airlines.service.PassengerLoginService;

@RestController
@RequestMapping("/api/flights/jwt")
public class PassengerControllerJWT {

	@Autowired
	private PassengerLoginService passengerLoginSerivce;
	
	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTService jwtService;

	// This endpoint (URL) will be accessed by the passenger having "USER" role
	@GetMapping("/checkuser")
	public String checkAfterLogin() {
		return "Welcome to the Airlines Application having USER role";
	}
	
	
	// This endpoint (URL) will be accessed by the passenger having "ADMIN" role
	@GetMapping("/checkadmin")
	public String checkAfterLoginForAdmin() {
		return "Welcome to the Airlines Application having ADMIN role";
	}

	
	// This End point(API) "/api/flights/jwt/login" is used for authenticating the customer (Sign in)

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginCheck(@RequestBody Passenger passenger) {
		
	    UsernamePasswordAuthenticationToken token =
	            new UsernamePasswordAuthenticationToken(passenger.getEmail(), passenger.getPwd());

	    try {
	        Authentication authenticate = authenticationManager.authenticate(token);

	        if (authenticate.isAuthenticated()) {
	        	
	        	// fetching role from the database (the authenticated Passenger)
	            Passenger dbPassenger = passengerRepository.findByEmail(passenger.getEmail());
	            
	            //Generate JWT token
	            String jwtToken = jwtService.generateToken(dbPassenger.getEmail(),dbPassenger.getRole());

	            // Success response
	            Map<String, Object> success = new HashMap<>();
	            success.put("message", "Login successful. Welcome to the Airlines Application!");
	            success.put("status", "SUCCESS");
	            success.put("role", dbPassenger.getRole());
	            success.put("token", jwtToken);

	            return new ResponseEntity<>(success, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        // 🔹 Error response
	        Map<String, Object> error = new HashMap<>();
	        error.put("message", "Invalid credentials, please check your email or password.");
	        error.put("status", "ERROR");

	        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	    }

	    // 🔹 Fallback (shouldn’t usually hit this)
	    Map<String, Object> unexpected = new HashMap<>();
	    unexpected.put("message", "Unexpected error occurred.");
	    unexpected.put("status", "ERROR");

	    return new ResponseEntity<>(unexpected, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// This End point(API) "/api/flights/jwt/register" is used for registering the customer with the application (Sign up)
	
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> saveCustomer(@RequestBody Passenger passenger) {

		String encodedPwd = passwordEncoder.encode(passenger.getPwd());
		passenger.setPwd(encodedPwd);
		
		// Default role (Each Passenger will be a user)
	    //passenger.setRole("USER");

		
		// Calling the Service Layer save() method for saving the customer
		boolean status = passengerLoginSerivce.save(passenger);
		
		
		Map<String, Object> response = new HashMap<>();

		// when successfully added (saved) the customer
		if (status) {
			response.put("message", "Registration successful.");
			response.put("email", passenger.getEmail());
			response.put("status", "CREATED");
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}

		// When failure happens while saving
		response.put("message", "Registration failed. Please try again later.");
		response.put("status", "ERROR");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
