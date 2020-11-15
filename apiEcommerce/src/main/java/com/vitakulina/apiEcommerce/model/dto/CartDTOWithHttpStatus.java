package com.vitakulina.apiEcommerce.model.dto;

import org.springframework.http.HttpStatus;

public class CartDTOWithHttpStatus {

	private CartDTO cartDTO;
	private HttpStatus httpStatus;
	
	public CartDTOWithHttpStatus() {
		super();
	}
	
	public CartDTO getCartDTO() {
		return cartDTO;
	}
	public void setCartDTO(CartDTO cartDTO) {
		this.cartDTO = cartDTO;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	
	
}
