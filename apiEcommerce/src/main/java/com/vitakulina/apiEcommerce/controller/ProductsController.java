package com.vitakulina.apiEcommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.service.impl.ProductServiceImpl;

@RestController
public class ProductsController {
	
	private ProductServiceImpl productService;
	
	public ProductsController(ProductServiceImpl productService) {
		super();
		this.productService = productService;
	}
	
	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts(){
		return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);		
	}
	
	
	@PostMapping(value = "/products")
	public ResponseEntity<Object> postProduct(@Valid @RequestBody ProductDTO productDTO){
		ProductDTO productCreatedDTO = productService.post(productDTO);
		return new ResponseEntity<>(productCreatedDTO, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/products/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id){
		return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
	}
	
	@PutMapping(value = {"/products", "/products/{id}"})
	public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Optional<Long> id){

		if(id.isPresent()) {
			productDTO.setId(id.get());			
		}
		//the cases where id is not present will be caught in the service
		ProductDTO productUpdated = productService.put(productDTO);
		return new ResponseEntity<>(productUpdated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = {"products", "/products/{id}"})
	public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Optional<Long> id){		
		return new ResponseEntity<>(productService.deleteProductById(id), HttpStatus.OK);
	}
	
	/*
	//Probando buscar los productos por nombre
	@GetMapping(value = "/products/find-name/{name}")
	public ResponseEntity<List<ProductDTO>> getProductById(@PathVariable String name){
		return new ResponseEntity<>(productService.getByProdName(name), HttpStatus.OK);
	}*/

}
