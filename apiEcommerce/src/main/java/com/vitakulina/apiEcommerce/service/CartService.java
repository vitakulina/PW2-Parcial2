package com.vitakulina.apiEcommerce.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductInCartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;

@Service
public interface CartService {
	
	CartDTO postNewCart(UserCartDTO userDetails);
	CartDTO postProductToCart(Long cartId, CartProductDTO cartProductDTO);
	CartDTO deleteProductFromCart(Long cartId, Long productId);
	Set<ProductInCartDTO> getProductsInCart(Long cartId);
	CartDTO getCart(Long cartId);
	CartDTO postCheckoutCart(Long cartId);
	
	
}