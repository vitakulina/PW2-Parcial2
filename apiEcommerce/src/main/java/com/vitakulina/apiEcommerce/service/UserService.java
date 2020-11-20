package com.vitakulina.apiEcommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.UserDTO;

@Service
public interface UserService {
	
	List<UserDTO> getAllUsers();
	UserDTO getUserById(Long id);
	UserDTO getUserByUsername(String username);
	UserDTO getUserByEmail(String email);
	UserDTO postRegisterUser(UserDTO userDTO);
	UserDTO putUpdateUser(UserDTO userDTO);
	List<UserDTO> getUsersByState(String state); //Por ej todos los usuarios bloqueados
	UserDTO deleteUser(Optional<Long> idOpt);
		

}
