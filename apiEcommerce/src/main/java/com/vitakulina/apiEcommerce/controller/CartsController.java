package com.vitakulina.apiEcommerce.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.service.impl.CartServiceImpl;

@RestController
public class CartsController {
	
	private CartServiceImpl cartService;
	
	public CartsController(CartServiceImpl cartService) {
		super();
		this.cartService = cartService;
	}
	
	
	@PostMapping(value="/carts")
	public ResponseEntity<CartDTO> createCart(@Valid @RequestBody UserCartDTO userCartDTO){
		CartDTO cart = cartService.postNewCart(userCartDTO);		
		return new ResponseEntity<>(cart, HttpStatus.CREATED);
	
	}

}
