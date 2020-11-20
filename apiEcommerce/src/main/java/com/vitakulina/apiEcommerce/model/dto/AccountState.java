package com.vitakulina.apiEcommerce.model.dto;

public enum AccountState {
	ACTIVE(1, "Active"),
	BLOCKED(2, "Blocked");
	

	private final Integer statusId;
	private final String statusPhrase;
	
	AccountState(int id, String status) {
		this.statusId = id;
		this.statusPhrase = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public String getStatusPhrase() {
		return statusPhrase;
	}
	
	
	
	
	

}
