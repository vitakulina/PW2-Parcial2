package com.vitakulina.apiEcommerce.service;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.UserDTO;

@Service
public interface UserRecoveryService {
	
	//void sendRecoveryEmail(String username);
	UserDTO sendRecoveryEmail(Long id);

}
