package com.vitakulina.apiEcommerce.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vitakulina.apiEcommerce.security.JwtRequest;
import com.vitakulina.apiEcommerce.security.JwtResponse;
import com.vitakulina.apiEcommerce.security.JwtTokenUtil;
import com.vitakulina.apiEcommerce.service.impl.JwtUserDetailsServiceImpl;
import com.vitakulina.apiEcommerce.service.impl.UserServiceImpl;

@RestController
public class JwtAuthenticationController {
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenUtil jwtTokenUtil;
	private JwtUserDetailsServiceImpl userDetailsService;
	
	private UserServiceImpl userService;
	
	public JwtAuthenticationController(AuthenticationManager authenticationManager,
			JwtTokenUtil jwtTokenUtil, JwtUserDetailsServiceImpl userDetailsService, UserServiceImpl userService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}
	/*
	@PostMapping(value = "/register")
	public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user){
		return new ResponseEntity<>(userDetailsService.save(user), HttpStatus.CREATED);
	}
	*/
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception{
		
		//authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		userService.validateLogin(authenticationRequest);
				
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
	}
	
	/*
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}*/

}
