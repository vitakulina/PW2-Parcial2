package com.vitakulina.apiEcommerce.service.business.exception;
import org.springframework.http.HttpStatus;


public enum UserError {
	
	USER_DUPLICATE_USERNAME("USER_DUPLICATE_USERNAME", "Username already exists", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_DUPLICATE_EMAIL("USER_DUPLICATE_EMAIL", "The provided email is already in use by an existant account", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_USERNAME_REQUIRED("USER_USERNAME_REQUIRED", "Username is a required field", HttpStatus.BAD_REQUEST),
	USER_PASSWORD_REQUIRED("USER_PASSWORD_REQUIRED", "Password is a required field", HttpStatus.BAD_REQUEST);

	
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
