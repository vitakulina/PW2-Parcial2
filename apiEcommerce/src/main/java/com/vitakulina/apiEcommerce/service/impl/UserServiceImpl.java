package com.vitakulina.apiEcommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.UUID;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.BlockedAccout;
import com.vitakulina.apiEcommerce.model.User;
import com.vitakulina.apiEcommerce.model.dto.RecoveryKeyState;
import com.vitakulina.apiEcommerce.model.dto.UserCreateDTO;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.model.dto.UserRecoveryDTO;
import com.vitakulina.apiEcommerce.model.dto.UserUpdateDTO;
import com.vitakulina.apiEcommerce.repository.BlockedAccoutRepository;
import com.vitakulina.apiEcommerce.repository.UserRepository;
import com.vitakulina.apiEcommerce.security.JwtRequest;
import com.vitakulina.apiEcommerce.service.UserService;
import com.vitakulina.apiEcommerce.service.business.exception.UserError;
import com.vitakulina.apiEcommerce.service.business.exception.UserException;





@Service
public class UserServiceImpl implements UserService {
	
	private final static String FROM = "accounts@tempano.com";
	private final static String SUBJECT_BLOCKED = "Recuperacion de usuario bloqueado";
	private final static String SUBJECT_FORGOT = "Recuperacion de password";
	
	
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired JwtUserDetailsServiceImpl jwtUserDetailService;
	
	private BlockedAccoutRepository blockedAccountRepo;
	
	public UserServiceImpl(UserRepository userRepo, BlockedAccoutRepository blockedAccountRepo) {
		super();
		this.userRepo = userRepo;
		this.blockedAccountRepo = blockedAccountRepo;
	}


	
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserDTO> usersDTO = new ArrayList<>();
		
		if(users != null) {
			users.forEach(u ->{
				UserDTO userDTO = new UserDTO();
				BeanUtils.copyProperties(u, userDTO);
				usersDTO.add(userDTO);
			});
		}
		
		return usersDTO;
		
	}

	@Override
	public UserDTO getUserById(Long id) {
		Optional<User> userOpt = userRepo.findById(id);
		UserDTO userDTO = new UserDTO();
		
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			BeanUtils.copyProperties(user, userDTO);
		}else {
			throw new UserException(UserError.USER_NOT_PRESENT);
		}
		return userDTO;
	}

	
	@Override
	public UserDTO getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public UserDTO postRegisterUser(UserCreateDTO userCreateDTO) {
		// TODO Auto-generated method stub		
		validateRegistrationForm(userCreateDTO);
		
		String passEncrypted = bcryptEncoder.encode(userCreateDTO.getPassword());
		User newUser = new User(userCreateDTO.getUsername(), passEncrypted);
		User savedUser = jwtUserDetailService.save(newUser);
		
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(savedUser, userDTO);
		return userDTO;
	}

	

	
	@Override
	public UserDTO putUpdateUser(Long id, UserUpdateDTO userUpdateDTO) {
		UserDTO userDTO = new UserDTO();
		
		Optional<User> userOpt = userRepo.findById(id);
		if(userOpt.isPresent()) {
			
			if(userUpdateDTO.getPassword() == null || userUpdateDTO.getPassword().trim().isEmpty()) {
				throw new UserException(UserError.USER_PASSWORD_REQUIRED);
			}
			
			validatePasswordFormat(userUpdateDTO.getPassword());
			
			User user = userOpt.get();
			user.setPassword(bcryptEncoder.encode(userUpdateDTO.getPassword()));
			
			userRepo.save(user);
			
			BeanUtils.copyProperties(user, userDTO);
			
		}else {
			throw new UserException(UserError.USER_NOT_PRESENT);
		}
		
		return userDTO;
	}
	
	
	@Override
	public void validateLogin(JwtRequest jwtRequest) {
		UserCreateDTO userDTO = new UserCreateDTO();
		BeanUtils.copyProperties(jwtRequest, userDTO);
		
		validateMandatoryUserFields(userDTO);
		
		User user = userRepo.findByUsername(jwtRequest.getUsername());
		if(user != null) {
			//chequeamos primero si no está bloqueado
			if(user.getIsBlocked().equalsIgnoreCase("true")) {
				throw new UserException(UserError.USER_IS_BLOCKED);
			}
			
			if(autenticatePassword(jwtRequest.getUsername(), jwtRequest.getPassword())) {
								
				//reseteamos el contador
				user.clearLoginAttempts();
				userRepo.save(user);
				
			}else {
				//aumentamos el contador de intentos
				user.incrementLoginAttempts();
				userRepo.save(user);
				
				//chequeamos si el usuario tiene 3 intentos y en ese caso bloqueamos
				if(user.getLoginAttempts() > 2) {
					user.setIsBlocked("true");	
					userRepo.save(user);
				}
								
				throw new UserException(UserError.USER_INVALID_CREDENTIALS);
			}
			
		}else{
			throw new UserException(UserError.USER_INVALID_CREDENTIALS);
		}				
		
	}
	
	private boolean  autenticatePassword(String username, String password) {
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return true;
		}catch (Exception e){
			return false; // ya confirmamos antes que el usuario es correcto, entonces el error esta en el password
			//throw new UserException(UserError.USER_INVALID_CREDENTIALS);
		}		
	}

	@Override
	public UserDTO deleteUser(Optional<Long> idOpt) {
		UserDTO userDTO = new UserDTO();
		
		if(idOpt.isPresent()) {
			Long id = idOpt.get();
			Optional<User> userOpt = userRepo.findById(id);
			if(userOpt.isPresent()) {
				User user = userOpt.get();
				BeanUtils.copyProperties(user, userDTO);
				userRepo.delete(user);
				
			}else {
				throw new UserException(UserError.USER_NOT_PRESENT);
			}
						
			
		}else {
			throw new UserException(UserError.USER_NOT_PRESENT);
		}
		return userDTO;
	}
	
	private void validateRegistrationForm(UserCreateDTO userDTO) {
		validateMandatoryUserFields(userDTO);
		
		if(userRepo.findByUsername(userDTO.getUsername()) != null) {
			throw new UserException(UserError.USER_USERNAME_ALREADY_EXISTS);
		}
		
		validateUsernameFormat(userDTO.getUsername());
		validatePasswordFormat(userDTO.getPassword());	
		
		
	}
	


	private void validateMandatoryUserFields(UserCreateDTO userDTO) {
		if(userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()){
			throw new UserException(UserError.USER_USERNAME_REQUIRED);
		}
		
		if(userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
			throw new UserException(UserError.USER_PASSWORD_REQUIRED);
		}
				
	}
	
	
	

	private void validateUsernameFormat(String username) {
		validateEmail(username);
		Pattern usernamePattern = Pattern.compile("^.{1,128}$"); //de 1 a 128 caracteres
		Matcher usernameMatcher = usernamePattern.matcher(username);
		if(!usernameMatcher.matches()) {
			throw new UserException(UserError.USER_USERNAME_INVALID);
		}
		
	}
	
	private void validatePasswordFormat(String pass) {
		Pattern passPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9\\.\\-_]{8,32}$"); 
		Matcher passMatcher = passPattern.matcher(pass);
		if(!passMatcher.matches()) {
			throw new UserException(UserError.USER_PASSWORD_INVALID);
		}
		
	}
	

	
	public static void validateEmail(String email) {
			
		 EmailValidator validator = EmailValidator.getInstance();

	      if(!validator.isValid(email)) {
	    	  throw new UserException(UserError.USER_USERNAME_INVALID);
	      }
	        		
			
		}
	
	public String generateRecoveryID(){
		return UUID.randomUUID().toString();
	}



	public UserRecoveryDTO sendRecovery(Long id) {
		
		UserRecoveryDTO message = new UserRecoveryDTO();
		Optional<User> userOpt = userRepo.findById(id);
		if(userOpt.isPresent()) {
			User user = userOpt.get();
			
			String key = generateRecoveryID();
			message.setKey(key);
			message.setEmail(user.getUsername());
			String subject;
			
			if(user.getIsBlocked().equalsIgnoreCase("true")) {
				subject = SUBJECT_BLOCKED;
			}else {
				subject = SUBJECT_FORGOT;
			}
			message.setSubject(subject);
			
			message.setMessage(getBodyRecovery(key));
			
			updateBlockedAccout(user, key);
		
		}else {
			throw new UserException(UserError.USER_NOT_PRESENT);
		}
		
		return message;
	}
	
	public String getBodyRecovery(String id) {
		String body ="Para recuperar el usuario confirme la solicitud haciendo click en el" + 
				"siguiente link:"
				+ "http://localhost:8080/users/recovery/" + id;
		return body;
	}
	
	void updateBlockedAccout(User user, String key) {
		BlockedAccout account = new BlockedAccout();
		account.setUser(user);
		account.setRecoveryKey(key);
		account.setKeyState(RecoveryKeyState.NEW);
		blockedAccountRepo.save(account);
		
	}

}
