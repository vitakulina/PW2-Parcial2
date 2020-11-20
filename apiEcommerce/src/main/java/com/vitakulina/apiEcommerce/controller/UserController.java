package com.vitakulina.apiEcommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.service.UserService;

@RestController
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping(value="/users")
	public ResponseEntity<List<UserDTO>> viewAllUsers(){
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
		
	}

}
