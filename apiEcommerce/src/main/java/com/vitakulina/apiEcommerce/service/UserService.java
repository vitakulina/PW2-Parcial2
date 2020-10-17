package com.vitakulina.apiEcommerce.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	
	//TODO: in implementation of save user need to check that the username is unique and launch an exception if it already exists

}
