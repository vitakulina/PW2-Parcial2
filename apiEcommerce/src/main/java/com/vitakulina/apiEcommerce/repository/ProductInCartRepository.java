package com.vitakulina.apiEcommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.ProductInCart;



public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long>{
	
	List<ProductInCart> findByCart(Cart cart);

}
