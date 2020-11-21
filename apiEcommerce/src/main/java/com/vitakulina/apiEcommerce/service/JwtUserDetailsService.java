package com.vitakulina.apiEcommerce.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.vitakulina.apiEcommerce.model.User;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;

public interface JwtUserDetailsService extends UserDetailsService{
	
	User save(User user);
}
