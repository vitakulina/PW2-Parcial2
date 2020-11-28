package com.vitakulina.apiEcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	List<User> findByIsActive (boolean isActive);


}
