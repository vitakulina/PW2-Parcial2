package com.vitakulina.apiEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);

	//User findByEmail(String email);

}
