package com.vitakulina.apiEcommerce.model;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vitakulina.apiEcommerce.model.dto.RecoveryKeyState;

@Entity
public class BlockedAccout {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	private String key;
	private RecoveryKeyState keyState;
	
	
	public BlockedAccout() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getRecoveryKey() {
		return key;
	}


	public void setRecoveryKey(String recoveryKey) {
		this.key = recoveryKey;
	}


	public RecoveryKeyState getKeyState() {
		return keyState;
	}


	public void setKeyState(RecoveryKeyState keyState) {
		this.keyState = keyState;
	}
	
	
	
	
	

	
	

}
