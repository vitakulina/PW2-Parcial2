package com.vitakulina.apiEcommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.ProductInCart;
import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.repository.ProductInCartRepository;
import com.vitakulina.apiEcommerce.repository.ProductRepository;
import com.vitakulina.apiEcommerce.service.ProductService;
import com.vitakulina.apiEcommerce.service.business.exception.ProductError;
import com.vitakulina.apiEcommerce.service.business.exception.ProductException;


@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	private final ProductInCartRepository productInCartRepo;
	
	public ProductServiceImpl (ProductRepository productRepository, ProductInCartRepository productInCartRepo) {
		super();
		this.productRepository = productRepository;
		this.productInCartRepo = productInCartRepo;
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
		}else {
			throw new ProductException(ProductError.NO_PRODUCTS_AVAILABLE);
		}
		return productosDTO;
	}

	@Override
	public ProductDTO post(ProductDTO productDTO) {
		if(productDTO.getId() == null) {
			if(isValidProduct(productDTO)) {
				Product product = new Product();
				BeanUtils.copyProperties(productDTO, product);
				product = productRepository.save(product);
				BeanUtils.copyProperties(product, productDTO);
			}			
			
		}else {
			//When adding a new product, the productDTO received as param should not have an id
			throw new ProductException(ProductError.PRODUCT_ID_NOT_REQUIRED);			
		}
		
		return productDTO;
	}
	
	

	@Override
	public ProductDTO getById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		ProductDTO prodDTO = new ProductDTO();
		
		if(product.isPresent()) {
			
			BeanUtils.copyProperties(product.get(), prodDTO);
		}else {
			throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);			
		}		
		return prodDTO;
	}

	
	@Override
	public ProductDTO put(ProductDTO productDTO) {
		if(productDTO.getId() == null) {
			throw new ProductException(ProductError.PRODUCT_ID_REQUIRED); 
		}
				
		Optional<Product> prod = productRepository.findById(productDTO.getId());
		if(prod.isPresent()) {
			if(isValidProduct(productDTO)) {
				Product product = prod.get();
				BeanUtils.copyProperties(productDTO, product);
				product = productRepository.save(product);
			}						
		}else {
			throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
		}		
		return productDTO;
	}

	
	@Override
	public ProductDTO deleteProductById(Optional<Long> idOpt) {
		if(idOpt.isPresent()) {
			Long id = idOpt.get();
			
			Optional<Product> product = productRepository.findById(id);
			ProductDTO prodDTO = new ProductDTO();
			
			if(product.isPresent()) {
				//Check if product is inside a cart before deleting
				Optional<List<ProductInCart>> productsInCart = productInCartRepo.findByProduct(product.get());
				if(productsInCart.isPresent()) {
					if(productsInCart.get().size() > 0) {
						System.out.println("Prod List size : " + productsInCart.get().size());
						throw new ProductException(ProductError.PRODUCT_PRESENT_IN_CART);
					}
				}
				BeanUtils.copyProperties(product.get(), prodDTO);
				productRepository.delete(product.get());
				
			}else {
				throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
			}
			
			return prodDTO;				
			
		}else {
			throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
		}
		
	}
	
	//function to validate fields for post and put
	public boolean isValidProduct(ProductDTO productDTO) {
		//validating product description
		if(productDTO.getDescription() == null || productDTO.getDescription().trim().isEmpty()) {
			throw new ProductException(ProductError.PRODUCT_DESCRIPTION_REQUIRED);
		}
		//validating stock
		if(productDTO.getStock() == null) {
			throw new ProductException(ProductError.PRODUCT_STOCK_REQUIRED);
		}else if(productDTO.getStock() <= 0) {
			throw new ProductException(ProductError.PRODUCT_STOCK_INVALID);
		}
		//validating price
		if(productDTO.getUnitPrice() == null) {
			throw new ProductException(ProductError.PRODUCT_UNITPRICE_REQUIRED);
		}else if((productDTO.getUnitPrice().compareTo(BigDecimal.ZERO) == -1) || 
				(productDTO.getUnitPrice().compareTo(BigDecimal.ZERO) == 0)) {
			
			throw new ProductException(ProductError.PRODUCT_UNITPRICE_INVALID);
		}
		return true;
	}
	
	
	//Testing JPA Repository with custom queries (find by prod name/description which contains (like) the passed name and it's case insensitive)
	public List<ProductDTO> getByProdName(String name){
		List<Product> productos = productRepository.findByDescriptionContainsIgnoreCase(name);
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

}
