package com.vitakulina.apiEcommerce.model.dto;

import java.math.BigDecimal;

public class ProductDTO {
	
	private Long id;
	private String description;
	private Integer stock;
	private BigDecimal unitPrice;
	
	
	public ProductDTO() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	
}
