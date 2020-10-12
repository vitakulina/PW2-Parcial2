package com.vitakulina.apiEcommerce.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.repository.ProductRepository;
import com.vitakulina.apiEcommerce.service.ProductService;

public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	public ProductServiceImpl (ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	
	@Override
	public List<ProductDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO post(ProductDTO productDTO) {
		//TODO implement validations on the mandatory fields
		Product product = new Product();
		BeanUtils.copyProperties(productDTO, product);
		product = productRepository.save(product);
		BeanUtils.copyProperties(product, productDTO);
		return productDTO;
	}
	
	

	@Override
	public ProductDTO getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO put(ProductDTO product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO deleteProduct(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isValidProduct(ProductDTO productDTO) {
		// TODO throw exceptions according to the missing/invalid parts in the product
		return true;
	}

}
