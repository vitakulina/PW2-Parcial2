package com.vitakulina.apiEcommerce.service.business.exception;

public class UserException extends RuntimeException {

	private static final long serialVersionUID = 7404126184182824336L;

	private UserError error;
	
	public UserException() {
		super();
	}
	
	public UserException(UserError error) {
		super();
		this.error = error;
	}

	public UserError getError() {
		return error;
	}

	public void setError(UserError error) {
		this.error = error;
	}
	
	
}
