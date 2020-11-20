package com.vitakulina.apiEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
	
	
	private String username;
	private String password;
	
	
	private String firstName;
	private String lastName;
	private String email;
	@JsonIgnore //TODO: considerar si mostrar el estado o no
	private AccountState state;
	@JsonIgnore 
	private Integer loginAttempts;
	
	
	public UserDTO() {
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

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public AccountState getState() {
		return state;
	}


	public void setState(AccountState state) {
		this.state = state;
	}


	public Integer getLoginAttempts() {
		return loginAttempts;
	}


	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	} 

	
	
	

}
