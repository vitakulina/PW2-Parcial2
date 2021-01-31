package com.vitakulina.apiEcommerce.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


public class CartDTO {
	
	private Long id;
	private String fullName;
	private String email;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate creationDate;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) //al crearse el carrito no tiene checkoutDate, esto evita mostrar el null en Json hasta que se hace el checkout
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private LocalDateTime checkoutDate;
	
	private Set<ProductDTO> products;
	
	private String status;
	private BigDecimal total;
	
	public CartDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public Set<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductDTO> products) {
		this.products = products;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public void addProduct(ProductDTO product) {
		this.products.add(product);
	}

	public LocalDateTime getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDateTime checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	
	
	
	

}
