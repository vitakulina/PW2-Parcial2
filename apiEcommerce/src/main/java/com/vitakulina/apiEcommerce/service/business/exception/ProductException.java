package com.vitakulina.apiEcommerce.service.business.exception;

public class ProductException extends RuntimeException{

	private static final long serialVersionUID = 822652046600278989L;
	
	private ProductError error;
	

	public ProductException() {
		super();
	}
	
	public ProductException(ProductError error) {
		super();
		this.error = error;
	}
	
	public ProductError getError() {
		return error;
	}

	public void setError(ProductError error) {
		this.error = error;
	}
	
	

}
