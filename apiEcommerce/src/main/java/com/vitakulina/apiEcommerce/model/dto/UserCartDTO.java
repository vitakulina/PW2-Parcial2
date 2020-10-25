package com.vitakulina.apiEcommerce.model.dto;

public class UserCartDTO {
	
	String fullName;
	String email;
	
	public UserCartDTO() {
		super();
	}
	
	public UserCartDTO(String fullName, String email) {
		super();
		this.fullName = fullName;
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
