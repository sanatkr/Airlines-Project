package com.airlines.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.airlines.service.AppFilter;
import com.airlines.service.JWTService;
import com.airlines.service.PassengerLoginService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfiguration {

	@Autowired
	private PassengerLoginService passengerLoginService;

	@Autowired
	private AppFilter filter;

	
	
	@Bean
	public PasswordEncoder pwdEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authProvider() {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passengerLoginService);

		authProvider.setPasswordEncoder(pwdEncoder());

		return authProvider;
	}

	// intercepting the coming requests to the application
	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {

		// without using jwt token (Session Based Authentication)
//		http
//        .authorizeHttpRequests(req -> req
//         .requestMatchers("/api/flights/register", "/api/flights/login").permitAll()
//            .anyRequest().authenticated()
//        )
//        .formLogin(Customizer.withDefaults()) // enables login form and session-based login
//        .logout(Customizer.withDefaults())    // enables logout endpoint
//        .csrf(csrf -> csrf.disable()); // disable CSRF for API testing in Postman
//
//    return http.build();

		// using jwt token 
		http
	    .csrf(csrf -> csrf.disable())
	    .authorizeHttpRequests(auth -> auth
	        .requestMatchers("/api/flights/jwt/register", "/api/flights/jwt/login").permitAll()
	        .requestMatchers("/api/flights/jwt/checkuser").hasRole("USER")   // only users
            .requestMatchers("/api/flights/jwt/checkadmin").hasRole("ADMIN")     // only admins
	        .anyRequest().authenticated()
	    )
	    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
	    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();

	}

}
