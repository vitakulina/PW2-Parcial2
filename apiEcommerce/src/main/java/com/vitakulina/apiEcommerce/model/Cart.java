package com.vitakulina.apiEcommerce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;




@Entity
@Table(name = "cart")
public class Cart {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String fullName;
	private String email;
	private LocalDate creationDate;
		
	@OneToMany(mappedBy = "cart")
	Set<ProductInCart> productsInCart;
	
	private String status;
	private BigDecimal total;
	
	public Cart() {
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

	public Set<ProductInCart> getProductsInCart() {
		return productsInCart;
	}

	public void setProductsInCart(Set<ProductInCart> productsInCart) {
		this.productsInCart = productsInCart;
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
	
		
	
	public void addProduct(ProductInCart product) {
		this.productsInCart.add(product);
	} 

}
