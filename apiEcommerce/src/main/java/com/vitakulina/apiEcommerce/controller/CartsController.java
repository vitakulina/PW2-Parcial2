package com.vitakulina.apiEcommerce.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductInCartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.service.impl.CartServiceImpl;

@RestController
public class CartsController {
	
	private CartServiceImpl cartService;
	
	public CartsController(CartServiceImpl cartService) {
		super();
		this.cartService = cartService;
	}
	
	@GetMapping(value="/carts")
	public ResponseEntity<List<CartDTO>> viewAllCarts(){
		List<CartDTO> carts = cartService.getAllCarts();
		return new ResponseEntity<>(carts, HttpStatus.OK);
	}
	
	@PostMapping(value="/carts")
	public ResponseEntity<CartDTO> createCart(@Valid @RequestBody UserCartDTO userCartDTO){
		CartDTO cart = cartService.postNewCart(userCartDTO);		
		return new ResponseEntity<>(cart, HttpStatus.CREATED);	
	}
	
	@PostMapping(value="/carts/{id}/products")
	public ResponseEntity<CartDTO> addProductToCart(@Valid @RequestBody CartProductDTO cartProductDTO, 
			@PathVariable(value="id") Long id){
		CartDTO cart = cartService.postProductToCart(id, cartProductDTO);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}
	
	
	@DeleteMapping(value="/carts/{cartId}/products/{productId}")
	public ResponseEntity<CartDTO> deleteProductFromCart(@PathVariable(value="cartId") Long cartId,
			@PathVariable(value="productId") Long productId){
		return new ResponseEntity<>(cartService.deleteProductFromCart(cartId, productId), HttpStatus.OK);
	}
		
	
	@GetMapping(value="/carts/{cartId}/products")
	public ResponseEntity<Set<ProductInCartDTO>> viewProductsInCart(@PathVariable(
			value="cartId") Long cartId){
		Set<ProductInCartDTO> products = cartService.getProductsInCart(cartId);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	
	
	@GetMapping(value="/carts/{id}")
	public ResponseEntity<CartDTO> viewCart(@PathVariable(value="id") Long id){
		CartDTO cart = cartService.getCart(id);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}
	
	@PostMapping(value="/carts/{id}/checkout")
	public ResponseEntity<CartDTO> checkoutCart(@PathVariable(value="id") Long id){
		return new ResponseEntity<>(cartService.postCheckoutCart(id), HttpStatus.OK);
	}
	

}
