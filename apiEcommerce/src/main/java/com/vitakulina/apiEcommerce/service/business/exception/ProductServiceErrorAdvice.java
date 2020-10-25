package com.vitakulina.apiEcommerce.service.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import com.vitakulina.apiEcommerce.model.dto.ErrorApi;

@ControllerAdvice
public class ProductServiceErrorAdvice extends ResponseEntityExceptionHandler{
	
	
	//Using Enums for error type with one Exception class for all the Product error types
	@ExceptionHandler({ProductException.class})
	public ResponseEntity<ErrorApi> handleProductException (ProductException e){
		return showError(e.getError().getHttpStatus(), e.getError().getErrCode(), e.getError().getErrMessage());
	}
	
	@ExceptionHandler({UserException.class})
	public ResponseEntity<ErrorApi> handleUserException (UserException e){
		return showError(e.getError().getHttpStatus(), e.getError().getErrCode(), e.getError().getErrMessage());
	}

	
	
	
	
	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ErrorApi> handleRuntimetException(RuntimeException e){
		return showError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", e.getMessage());
	}
	
	
	private ResponseEntity<ErrorApi> showError(HttpStatus status, String errorCode, String errorMessage){
		return new ResponseEntity<>(new ErrorApi(errorCode, errorMessage), status);
	}

}
