package com.vitakulina.apiEcommerce.service.business.exception;

import org.springframework.http.HttpStatus;

public enum CartError {
	
	CART_NOT_PRESENT("CART_NOT_PRESENT", "Cart not present in repository", HttpStatus.NOT_FOUND),
	CART_FULLNAME_REQUIRED("CART_FULLNAME_REQUIRED", "Cart fullName is required", HttpStatus.BAD_REQUEST),
	CART_EMAIL_REQUIRED("CART_EMAIL_REQUIRED", "Cart email is required", HttpStatus.BAD_REQUEST),
	PRODUCT_QUANTITY_REQUIRED("PRODUCT_QUANTITY_REQUIRED", "Product quantity is required", HttpStatus.BAD_REQUEST),
	PRODUCT_QUANTITY_INVALID("PRODUCT_QUANTITY_INVALID", "Product quantity must be greater than 0", HttpStatus.BAD_REQUEST),
	CART_PROCESSING_NOT_ALLOW_DELETION("CART_PROCESSING_NOT_ALLOW_DELETION", "Cart processing. Deletion is not possible", HttpStatus.CONFLICT),
	CART_STATUS_NOT_ALLOW_CHECKOUT("CART_STATUS_NOT_ALLOW_CHECKOUT", "Cart status not allow checkout", HttpStatus.CONFLICT),
	CART_INSUFFICIENT_PRODUCT_STOCK("CART_INSUFFICIENT_PRODUCT_STOCK", "Product quantity requested is above the available product stock", HttpStatus.BAD_REQUEST);
	
	private final String errCode;
	private final String errMessage;
	private final HttpStatus httpStatus;
	
	CartError(String errCode, String errMessage, HttpStatus httpStatus){
		this.errCode = errCode;
		this.errMessage = errMessage;
		this.httpStatus = httpStatus;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}

	