package com.vitakulina.apiEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
	
	private Long id;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	@JsonProperty("is_blocked")
	private boolean isBlocked;
	
	@JsonProperty("is_active")
	private boolean isActive;
	
	@JsonIgnore
	private Integer loginAttempts;
	
	
	public UserDTO() {
		super();
	}


	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public boolean getIsBlocked() {
		return isBlocked;
	}



	public void setIsBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}



	public boolean getIsActive() {
		return isActive;
	}



	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
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


	public Integer getLoginAttempts() {
		return loginAttempts;
	}


	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	} 

	
	
	

}
