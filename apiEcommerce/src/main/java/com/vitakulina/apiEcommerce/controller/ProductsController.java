package com.vitakulina.apiEcommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.service.impl.ProductServiceImpl;

@RestController
public class ProductsController {
	
	ProductServiceImpl productService;
	
	public ProductsController(ProductServiceImpl productService) {
		this.productService = productService;
	}
	
	@PostMapping(value = "/products")
	public ResponseEntity<Object> postProduct(@RequestBody ProductDTO productDTO){
		//TODO create a service to save products and return the created product
		ProductDTO productCreatedDTO = productService.post(productDTO);
		return new ResponseEntity<>(productCreatedDTO, HttpStatus.CREATED);
	}
	

}
