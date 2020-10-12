package com.vitakulina.apiEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
