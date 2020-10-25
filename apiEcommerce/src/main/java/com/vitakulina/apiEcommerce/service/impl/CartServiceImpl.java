package com.vitakulina.apiEcommerce.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductInCartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.service.CartService;
import com.vitakulina.apiEcommerce.service.business.exception.CartError;
import com.vitakulina.apiEcommerce.service.business.exception.CartException;

import java.time.LocalDate;


@Service
public class CartServiceImpl implements CartService {




	@Override
	public CartDTO postNewCart(UserCartDTO userDetails) {
		validateUserDetails(userDetails);
		CartDTO cart = new CartDTO(); //TODO need to create a new Cart and then copy to dto for the return
		cart.setFullName(userDetails.getFullName());
		cart.setEmail(userDetails.getEmail());
		LocalDate today = LocalDate.now();
		System.out.println("Local date: " + today);
		cart.setCreationDate(today);
		String dateString = today.toString();
		System.out.println("Date as string: " + dateString);
		return cart;	// return getCartDTO(cart);	
	}

	

	@Override
	public CartDTO postProductToCart(Long cartId, CartProductDTO cartProductDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO deleteProductFromCart(Long cartId, Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ProductInCartDTO> getProductsInCart(Long cartId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO getCart(Long cartId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO postCheckoutCart(Long cartId) {
		// TODO Auto-generated method stub
		// el status not allow checkout err seguramente es si el Set de prod dto esta vacio
		return null;
	}
	
	
	private void validateUserDetails(UserCartDTO userDetails) {
		//validate presence of fullName and email
		if(userDetails.getFullName() == null || userDetails.getFullName().trim().isEmpty()) {
			throw new CartException(CartError.CART_FULLNAME_REQUIRED);
		}
		if(userDetails.getEmail() == null || userDetails.getEmail().trim().isEmpty()) {
			throw new CartException(CartError.CART_EMAIL_REQUIRED);
		}
		
	}

}
