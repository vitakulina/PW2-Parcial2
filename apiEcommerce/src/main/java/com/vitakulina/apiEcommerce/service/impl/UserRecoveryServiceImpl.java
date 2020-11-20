package com.vitakulina.apiEcommerce.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.User;
import com.vitakulina.apiEcommerce.repository.UserRepository;
import com.vitakulina.apiEcommerce.service.UserRecoveryService;
import com.vitakulina.apiEcommerce.service.business.exception.UserError;
import com.vitakulina.apiEcommerce.service.business.exception.UserException;

@Service
public class UserRecoveryServiceImpl implements UserRecoveryService {

	
	private final static String FROM = "accounts-do-not-reply@ecommerce.com";
	private final static String SUBJECT_BLOCKED = "Recover blocked user";
	private final static String SUBJECT_FORGOT = "Change password";
	
	private UserRepository userRepo;
	private SendEmailServiceImpl emailService;
	
	
	public UserRecoveryServiceImpl(UserRepository userRepo, SendEmailServiceImpl emailService) {
		super();
		this.userRepo = userRepo;
		this.emailService = emailService;
	}
	
	
	@Override
	public void sendRecoveryEmail(String username) {
		
		User user = userRepo.findByUsername(username);
		if(user != null) {
			
			String firstName = user.getFirstName();
			String email = user.getEmail();
			String idRecupero = generateRecoveryID();
			String subject;
			
			if(isUserBlocked(user)) {
				subject = SUBJECT_BLOCKED;
			}else {
				subject = SUBJECT_FORGOT;
			}
			
			String body = getBodyRecovery(firstName, idRecupero);
			emailService.sendEmail(FROM, email, subject, body);
			
		}else {
			throw new UserException(UserError.USER_NOT_FOUND);
		}
	

	}


	private java.lang.String getBodyRecovery(java.lang.String firstName, java.lang.String idRecupero) {
		// TODO Auto-generated method stub
		return null;
	}


	private boolean isUserBlocked(User user) {
		// TODO Auto-generated method stub
		return false;
	}


	private String generateRecoveryID() {
		// TODO URL encode the recovery link and key
		return null;
	}

}
