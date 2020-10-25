package com.vitakulina.apiEcommerce.service.business.exception;
import org.springframework.http.HttpStatus;


public enum UserError {
	
	USER_DUPLICATE_USERNAME("USER_DUPLICATE_USERNAME", "Username already exists", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_DUPLICATE_EMAIL("USER_DUPLICATE_EMAIL", "The provided email is already in use by an existant account", HttpStatus.UNPROCESSABLE_ENTITY);
	
	//TODO: add username and passowrd as mandatory, throwing exception if not present
	
	private final String errCode;
	private final String errMessage;
	private final HttpStatus httpStatus;
	
	UserError(String errCode, String errMessage, HttpStatus httpStatus){
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
