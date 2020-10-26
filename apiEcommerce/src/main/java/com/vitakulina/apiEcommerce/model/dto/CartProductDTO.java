package com.vitakulina.apiEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartProductDTO {

	@JsonProperty("id")
	private Long productId;
	
	private Integer quantity;
	
	public CartProductDTO(){
		super();
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
