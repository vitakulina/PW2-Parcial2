package com.vitakulina.apiEcommerce.model.dto;

public enum CartStatus {
	NEW("NEW"),
	READY("READY"),
	PROCESSED("PROCESSED"),
	FAILED("FAILED");

	
	private final String status;
	
	CartStatus(String status){
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	
}
