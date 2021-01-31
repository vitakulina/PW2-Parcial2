package com.vitakulina.apiEcommerce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private LocalDateTime checkoutDate;
		
	@OneToMany(mappedBy = "cart", fetch=FetchType.EAGER) //agregado fetchType Eager sino las pruebas de procesamiento de carritos fallaban (porque iniciaba carritos lazily y no traia el set de productos)
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
	
	
	public void removeProduct(ProductInCart product) {
		this.productsInCart.remove(product);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public LocalDateTime getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDateTime checkoutDate) {
		this.checkoutDate = checkoutDate;
	} 
	
	

}
