package com.vitakulina.apiEcommerce.service.business.exception;

public class ProductNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -8310190033779010993L;

	public final static String ERROR_CODE = "PRODUCT_NOT_PRESENT";
	public final static String ERROR_MESSAGE = "Product not present in repository";
	
	
	public ProductNotFoundException() {
		super(ERROR_MESSAGE);
	}
	public String getErrorCode() {
		return ERROR_CODE;
	}
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}
	
	
}
