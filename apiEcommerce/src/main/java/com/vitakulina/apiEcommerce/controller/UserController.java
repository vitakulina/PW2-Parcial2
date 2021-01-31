package com.vitakulina.apiEcommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.UserCreateDTO;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.model.dto.UserRecoveryDTO;
import com.vitakulina.apiEcommerce.model.dto.UserUpdateDTO;
import com.vitakulina.apiEcommerce.service.impl.UserRecoveryServiceImpl;
import com.vitakulina.apiEcommerce.service.impl.UserServiceImpl;

@RestController
public class UserController {
	
	private UserServiceImpl userService;
	private UserRecoveryServiceImpl userRecoveryService;
	
	public UserController(UserServiceImpl userService, UserRecoveryServiceImpl userRecoveryService) {
		super();
		this.userService = userService;
		this.userRecoveryService = userRecoveryService;
	}
	
	@GetMapping(value="/users")
	public ResponseEntity<List<UserDTO>> viewAllUsers(){
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
		
	}
	
	@GetMapping(value="/users/{id}")
	public ResponseEntity<UserDTO> viewUser(@PathVariable(value="id") Long id){
		UserDTO user = userService.getUserById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
		
	}
	
	@PostMapping(value="/users/create")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO){
		UserDTO user = userService.postRegisterUser(userCreateDTO);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/users/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable(value="id") Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
		UserDTO user = userService.putUpdateUser(id, userUpdateDTO);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@DeleteMapping(value = {"/users", "/users/{id}"})
	public ResponseEntity<UserDTO> deleteUser(@PathVariable(value="id") Optional<Long> id){
		return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
	}
	
	@GetMapping(value="/users/recovery/send/{id}")
	public ResponseEntity<UserRecoveryDTO> sendRecovery(@PathVariable(value="id") Long id){
		UserRecoveryDTO message = userService.sendRecovery(id);
		return new ResponseEntity<>(message, HttpStatus.OK);
		
	}
	
	@GetMapping(value="/users/recovery/process/{key}")
	public ResponseEntity<?>  processRecovery(@PathVariable(value="key") String key){
		userService.recoverAccount(key);
		return new ResponseEntity<>(HttpStatus.OK);
	}


}
