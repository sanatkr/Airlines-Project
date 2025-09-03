package com.airlines.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airlines.entity.Passenger;
import com.airlines.service.PassengerLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/flights")
public class PassengerController {
	
	@Autowired
	private PassengerLoginService passengerLoginSerivce;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/check")
	public String checkAfterLogin()
	{
		return "Welcome to the Airlines Management Spring Security Project with session management";
	}
	
	@GetMapping("/check1")
	public ResponseEntity<String> checkFlights(Authentication authentication) {
	    return ResponseEntity.ok("Hello, " + (authentication != null ? authentication.getName() : "Anonymous"));
	}

	
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginCheck(@RequestBody Passenger passenger,HttpServletRequest request) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(passenger.getEmail(),
				passenger.getPwd());

		try {
			Authentication authenticate = authenticationManager.authenticate(token);
			
			if (authenticate.isAuthenticated()) {
	            // Store authentication in SecurityContext
	            SecurityContextHolder.getContext().setAuthentication(authenticate);

	            // ✅ Create a session (or reuse) and bind SecurityContext to it
	            HttpSession session = request.getSession(true);
	            session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	                SecurityContextHolder.getContext()
	            );

	            // ✅ Build response JSON
	            Map<String, Object> response = new HashMap<>();
	            response.put("message", "Login successful. Welcome to the Airlines Application!");
	            response.put("sessionId", session.getId()); // JSESSIONID for Postman testing
	            response.put("user", authenticate.getName());

	            return new ResponseEntity<>(response, HttpStatus.OK);
	            
	        }
		
//			if (authenticate.isAuthenticated()) {
//				
//				return new ResponseEntity<>("Welcome to the Airlines Application", HttpStatus.OK);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("error", "Invalid credentials, please try again."));
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerCustomer(@RequestBody Passenger passenger) {

		String encodedPwd = passwordEncoder.encode(passenger.getPwd());
		passenger.setPwd(encodedPwd);

		boolean status = passengerLoginSerivce.save(passenger);
		
		if(status)
		{
			return new ResponseEntity<>("Customer Registered", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Customer Not Registered", HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate(); // kills session
	    }
	    SecurityContextHolder.clearContext();

	    return ResponseEntity.ok(Map.of("message", "You have been logged out successfully."));
	}


	

}
