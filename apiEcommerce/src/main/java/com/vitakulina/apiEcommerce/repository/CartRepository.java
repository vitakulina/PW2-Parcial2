package com.vitakulina.apiEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
