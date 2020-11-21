package com.vitakulina.apiEcommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.UserCreateDTO;
import com.vitakulina.apiEcommerce.model.dto.UserDTO;
import com.vitakulina.apiEcommerce.model.dto.UserUpdateDTO;
import com.vitakulina.apiEcommerce.security.JwtRequest;

@Service
public interface UserService {
	
	List<UserDTO> getAllUsers();
	UserDTO getUserById(Long id);
	UserDTO getUserByUsername(String username);
	UserDTO postRegisterUser(UserCreateDTO userCreateDTO);
	UserDTO putUpdateUser(Long id, UserUpdateDTO userDTO);
	UserDTO deleteUser(Optional<Long> idOpt);
	void validateLogin(JwtRequest jwtRequest);
		

}
