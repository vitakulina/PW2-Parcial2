package com.vitakulina.apiEcommerce.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.repository.UserRepository;
import com.vitakulina.apiEcommerce.service.UserService;
import com.vitakulina.apiEcommerce.service.business.exception.UserError;
import com.vitakulina.apiEcommerce.service.business.exception.UserException;



@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepo;
	
	public UserServiceImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	//TODO: JwtUser podria ser una dependencia inyectada para user el encriptador
	
	@Override
	public List<UserDTO> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO getUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO postRegisterUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		
		validateRegistrationForm(userDTO);
		
		return null;
	}

	@Override
	public UserDTO putUpdateUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDTO> getUsersByState(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO deleteUser(Optional<Long> idOpt) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void validateRegistrationForm(UserDTO userDTO) {
		validateMandatoryUserFields(userDTO);
		
		if(userRepo.findByUsername(userDTO.getUsername()) != null) {
			throw new UserException(UserError.USER_DUPLICATE_USERNAME);
		}
		
		validateUsernameFormat(userDTO.getUsername());
		validatePasswordFormat(userDTO.getPassword());
		validateEmail(userDTO.getEmail());
		
	}
	


	private void validateMandatoryUserFields(UserDTO userDTO) {
		if(userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()){
			throw new UserException(UserError.USER_USERNAME_REQUIRED);
		}
		
		if(userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
			throw new UserException(UserError.USER_PASSWORD_REQUIRED);
		}
		
		if(userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
			throw new UserException(UserError.USER_EMAIL_REQUIRED);
		}
		if(userDTO.getFirstName() == null || userDTO.getFirstName().trim().isEmpty()) {
			throw new UserException(UserError.USER_FIRSTNAME_REQUIRED);
		}
		if(userDTO.getLastName() == null || userDTO.getLastName().trim().isEmpty()) {
			throw new UserException(UserError.USER_LASTNAME_REQUIRED);
		}
		
	}
	

	private void validateUsernameFormat(String username) {
		Pattern usernamePattern = Pattern.compile("^.{4,128}$"); //de 4 a 128 caracteres
		Matcher usernameMatcher = usernamePattern.matcher(username);
		if(!usernameMatcher.matches()) {
			throw new UserException(UserError.INVALID_USERNAME_FORMAT);
		}
		
	}
	
	private void validatePasswordFormat(String pass) {
		//FIXME: este chequeo deberia ser antes de encriptar la clave
		Pattern passPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9\\.\\-_]{8,32}$"); 
		Matcher passMatcher = passPattern.matcher(pass);
		if(!passMatcher.matches()) {
			throw new UserException(UserError.INVALID_PASSWORD_FORMAT);
		}
		
	}
	
	public static void validateEmail(String email) {
			
			try {
				InternetAddress emailAddr = new InternetAddress(email);
				emailAddr.validate();
			} catch (AddressException e) {
				e.printStackTrace();
				throw new UserException(UserError.INVALID_EMAIL_FORMAT);
			}
			
		}

}
