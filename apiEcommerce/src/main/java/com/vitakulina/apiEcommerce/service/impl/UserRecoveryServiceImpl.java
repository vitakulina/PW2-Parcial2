package com.vitakulina.apiEcommerce.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.User;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;
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
	public UserDTO sendRecoveryEmail(Long id) {
		UserDTO userDTO = new UserDTO();
		//User user = userRepo.findByUsername(username);
		Optional<User> userOpt = userRepo.findById(id);
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			/*
			String firstName = user.getFirstName();
			String email = user.getEmail();
			String idRecupero = generateRecoveryID();
			String subject;
			*/
			String firstName = user.getUsername();
			String subject ="test";
			String email = "test@test.com";
			String idRecupero = "123";
			
			BeanUtils.copyProperties(user, userDTO);
			/*
			if(isUserBlocked(user)) {
				subject = SUBJECT_BLOCKED;
			}else {
				subject = SUBJECT_FORGOT;
			}
			*/
			
			String body = getBodyRecovery(firstName, idRecupero);
			emailService.sendEmail(FROM, email, subject, body);
			
		}else {
			throw new UserException(UserError.USER_NOT_PRESENT);
		}
		
		return userDTO;
	

	}


	private String getBodyRecovery(String name, String id) {
		String body = "Hola " + name + ",\n \nPara recuperar tu usuario en Tempano de Hielo cliquea en el link de abajo e ingresa una nueva clave.\n \n"
				+ "http://localhost:8080/TP1VitaKulina/recuperarUsuario?key=" + id
				+ "\n \nSaludos\nEquipo Tempano de Hielo";
		return body;
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
