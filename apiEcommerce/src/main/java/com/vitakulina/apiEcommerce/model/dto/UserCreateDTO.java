package com.vitakulina.apiEcommerce.model.dto;



public class UserCreateDTO {
	
	private String username;
	
	private String password;
	
	public UserCreateDTO() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
