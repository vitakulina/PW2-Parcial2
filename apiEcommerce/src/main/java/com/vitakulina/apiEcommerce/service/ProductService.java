package com.vitakulina.apiEcommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.dto.ProductDTO;

@Service
public interface ProductService {
	
	List<ProductDTO> getAll();
	
	ProductDTO post (ProductDTO productDTO);
	
	ProductDTO getById (Long id);
	
	ProductDTO put (ProductDTO productDTO);
	
	ProductDTO deleteProductById (Optional<Long> idOpt);
	
	
	

}
