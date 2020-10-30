package com.vitakulina.apiEcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.ProductInCart;



public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long>{
	
	List<ProductInCart> findByCart(Cart cart);
	Optional<List<ProductInCart>> findByProduct(Product product);

}
