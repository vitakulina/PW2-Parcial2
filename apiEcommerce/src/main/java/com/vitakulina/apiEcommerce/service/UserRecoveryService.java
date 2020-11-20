package com.vitakulina.apiEcommerce.service;

import org.springframework.stereotype.Service;

@Service
public interface UserRecoveryService {
	
	void sendRecoveryEmail(String username);

}
