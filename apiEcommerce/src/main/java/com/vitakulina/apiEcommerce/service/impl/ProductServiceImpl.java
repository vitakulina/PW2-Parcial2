package com.vitakulina.apiEcommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.repository.ProductRepository;
import com.vitakulina.apiEcommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	public ProductServiceImpl (ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	
	@Override
	public List<ProductDTO> getAll() {
		List<Product> productos = productRepository.findAll();
		List<ProductDTO> productosDTO = new ArrayList<>();
		
		if(productos != null) {
			for(Product product : productos) {
				ProductDTO prodDTO = new ProductDTO();
				BeanUtils.copyProperties(product, prodDTO);
				productosDTO.add(prodDTO);
			}
			
		}
		return productosDTO;
	}

	@Override
	public ProductDTO post(ProductDTO productDTO) {
		//TODO implement validations on the mandatory fields and throw corresponding exceptions
		Product product = new Product();
		BeanUtils.copyProperties(productDTO, product);
		product = productRepository.save(product);
		BeanUtils.copyProperties(product, productDTO);
		return productDTO;
	}
	
	

	@Override
	public ProductDTO getById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		ProductDTO prodDTO = new ProductDTO();
		
		if(product.isPresent()) {
			
			BeanUtils.copyProperties(product.get(), prodDTO);
		}else {
			//TODO throw prod not found exception			
		}		
		return prodDTO;
	}

	
	@Override
	public ProductDTO put(ProductDTO productDTO) {
		//TODO implement validations on the mandatory fields and throw corresponding exceptions
		//TODO check if prod id exists
		Optional<Product> prod = productRepository.findById(productDTO.getId());
		if(prod.isPresent()) {
			Product product = prod.get();
			BeanUtils.copyProperties(productDTO, product);
			product = productRepository.save(product);			
		}else {
			//TODO throw prod id not found
		}
		
		return productDTO;
	}

	@Override
	public ProductDTO deleteProductById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		ProductDTO prodDTO = new ProductDTO();
		
		if(product.isPresent()) {
			BeanUtils.copyProperties(product.get(), prodDTO);
			productRepository.delete(product.get());
			
		}else {
			//TODO throw prod id not found
		}
		
		return prodDTO;
	}
	
	public boolean isValidProduct(ProductDTO productDTO) {
		// TODO throw exceptions according to the missing/invalid parts in the product
		return true;
	}

}
