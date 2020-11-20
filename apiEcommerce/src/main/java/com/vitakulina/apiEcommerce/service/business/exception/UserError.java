package com.vitakulina.apiEcommerce.service.business.exception;
import org.springframework.http.HttpStatus;


public enum UserError {
	
	USER_DUPLICATE_USERNAME("USER_DUPLICATE_USERNAME", "Username already exists", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_DUPLICATE_EMAIL("USER_DUPLICATE_EMAIL", "The provided email is already in use by an existant account", HttpStatus.UNPROCESSABLE_ENTITY),
	USER_USERNAME_REQUIRED("USER_USERNAME_REQUIRED", "Username is a required field", HttpStatus.BAD_REQUEST),
	USER_PASSWORD_REQUIRED("USER_PASSWORD_REQUIRED", "Password is a required field", HttpStatus.BAD_REQUEST),
	USER_EMAIL_REQUIRED("USER_EMAIL_REQUIRED", "Email is a required field", HttpStatus.BAD_REQUEST),
	USER_FIRSTNAME_REQUIRED("USER_FIRSTNAME_REQUIRED", "Firstname is a required field", HttpStatus.BAD_REQUEST),
	USER_LASTNAME_REQUIRED("USER_LASTNAME_REQUIRED", "Lastname is a required field", HttpStatus.BAD_REQUEST),
	USER_LOGIN_INVALID("USER_LOGIN_INVALID", "The provided login credentials are invalid", HttpStatus.BAD_REQUEST),
	INVALID_EMAIL_FORMAT("INVALID_EMAIL_FORMAT", "Invalid email format. Please provide a valid email", HttpStatus.BAD_REQUEST),
	INVALID_USERNAME_FORMAT("INVALID_USERNAME_FORMAT", "Username should have between 4 and 128 characters", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD_FORMAT("INVALID_PASSWORD_FORMAT", "Password should be between 8 and 32 characters and contain at least one number and one letter. No special characters except (.-_) are allowed.", HttpStatus.BAD_REQUEST);
	
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
