package com.vitakulina.apiEcommerce.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.User;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.repository.UserRepository;
import com.vitakulina.apiEcommerce.service.JwtUserDetailsService;
import com.vitakulina.apiEcommerce.service.business.exception.UserError;
import com.vitakulina.apiEcommerce.service.business.exception.UserException;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	/*
	@Autowired
	private PasswordEncoder bcryptEncoder;
	*/
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	
	
	
	@Override
	public User save(User user) {
		

		return userRepo.save(user);
		
	} 
		
		/*
		String email = null;
		if(userDto.getEmail() != null) {
			email = userRepo.findByEmail(userDto.getEmail()).getEmail(); 
		} 
		
		if(userRepo.findByUsername(userDto.getUsername()) != null) {
			throw new UserException(UserError.USER_DUPLICATE_USERNAME);
		}else if(email != null && email == userDto.getEmail()) {
			System.out.println("Email: " + userDto.getEmail());
			throw new UserException(UserError.USER_DUPLICATE_EMAIL);
		}else {
			newUser.setUsername(userDto.getUsername());
			newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
			newUser.setFirstName(userDto.getFirstName());
			newUser.setLastName(userDto.getLastName());
			newUser.setEmail(userDto.getEmail());
			return userRepo.save(newUser);
		}*/
		
	/*
	
	private boolean isValidUser(UserDTO userDto) {
		//validate empty fields
		if(userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
			throw new UserException(UserError.USER_PASSWORD_REQUIRED);
		}
		
		if(userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
			throw new UserException(UserError.USER_USERNAME_REQUIRED);
		}else if(userRepo.findByUsername(userDto.getUsername()) != null) {
			throw new UserException(UserError.USER_USERNAME_ALREADY_EXISTS);
		}		
		return true;
	} */

}
