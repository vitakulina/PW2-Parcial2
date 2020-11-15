package com.vitakulina.apiEcommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartDTOWithHttpStatus;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductInCartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;

@Service
public interface CartService {
	
	List<CartDTO>getAllCarts();
	CartDTOWithHttpStatus postNewCart(UserCartDTO userDetails);
	CartDTO postProductToCart(Long cartId, CartProductDTO cartProductDTO);
	CartDTO deleteProductFromCart(Long cartId, Long productId);
	Set<ProductInCartDTO> getProductsInCart(Long cartId);
	CartDTO getCart(Long cartId);
	CartDTO postCheckoutCart(Long cartId);
	List<CartDTO> getCartsByEmail(String email);
	List<CartDTO> getCartsByStatus(Optional<String> string);
	
	
}
