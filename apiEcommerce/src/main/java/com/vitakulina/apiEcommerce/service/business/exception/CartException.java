package com.vitakulina.apiEcommerce.service.business.exception;

public class CartException extends RuntimeException{

	private static final long serialVersionUID = -2860833399775029504L;
	
	private CartError error;
	
	public CartException() {
		super();
	}
	
	public CartException(CartError error) {
		super();
		this.error = error;
	}

	public CartError getError() {
		return error;
	}

	public void setError(CartError error) {
		this.error = error;
	}
	
	

}
