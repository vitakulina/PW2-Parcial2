package com.vitakulina.apiEcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	List<Cart> findByStatusIgnoreCase(String status);
	List<Cart> findByEmailIgnoreCase(String email);
	List<Cart> findByEmailAndStatusAllIgnoreCase(String email, String status);
	

}
