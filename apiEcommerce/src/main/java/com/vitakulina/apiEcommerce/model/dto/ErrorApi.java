package com.vitakulina.apiEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"error_code", "message"})
public class ErrorApi {
	
	@JsonProperty("error_code")
	private String errorCode;
	private String message;
	
	public ErrorApi() {
		super();
	}
	
	public ErrorApi(String errorCode, String message){
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
