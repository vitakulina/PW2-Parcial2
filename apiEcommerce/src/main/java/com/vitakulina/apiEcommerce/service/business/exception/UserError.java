package com.vitakulina.apiEcommerce.service.business.exception;
import org.springframework.http.HttpStatus;


public enum UserError {
	
	USER_USERNAME_ALREADY_EXISTS("USER_USERNAME_ALREADY_EXISTS", "User username already exist ", HttpStatus.CONFLICT),
	USER_USERNAME_REQUIRED("USER_USERNAME_REQUIRED", "User username is required", HttpStatus.BAD_REQUEST),
	USER_PASSWORD_REQUIRED("USER_PASSWORD_REQUIRED", "User password is required", HttpStatus.BAD_REQUEST),
	USER_NOT_PRESENT("USER_NOT_PRESENT", "User not present in repository ", HttpStatus.NOT_FOUND),
	USER_USERNAME_INVALID("USER_USERNAME_INVALID", "User username must be a valid emai", HttpStatus.BAD_REQUEST),
	USER_PASSWORD_INVALID("USER_PASSWORD_INVALID", "User password must be a valid: Minimum length 8, Maximum length 32. At least 1 char, At least 1 number. Not allowed special characters, only (- _ .)", HttpStatus.BAD_REQUEST),
	USER_RECOVERY_INVALID("USER_RECOVERY_INVALID", "Invalid data for user recovery", HttpStatus.BAD_REQUEST),
	USER_INVALID_CREDENTIALS("USER_INVALID_CREDENTIALS", "Invalid credentials", HttpStatus.BAD_REQUEST),
	USER_IS_BLOCKED("USER_IS_BLOCKED", "User blocked", HttpStatus.CONFLICT);
	
	
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
