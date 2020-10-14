package com.vitakulina.apiEcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	
	List<Product> findByDescriptionContainsIgnoreCase(String name);

}
