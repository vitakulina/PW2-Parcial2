package com.vitakulina.apiEcommerce.model.dto;

public enum CartStatus {
	NEW("NEW"),
	READY("READY");
	
	private final String status;
	
	CartStatus(String status){
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
	
	
}
