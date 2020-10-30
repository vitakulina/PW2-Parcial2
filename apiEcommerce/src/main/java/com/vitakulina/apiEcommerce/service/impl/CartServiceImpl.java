package com.vitakulina.apiEcommerce.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.ProductInCart;
import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.CartStatus;
import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductInCartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.repository.CartRepository;
import com.vitakulina.apiEcommerce.repository.ProductInCartRepository;
import com.vitakulina.apiEcommerce.repository.ProductRepository;
import com.vitakulina.apiEcommerce.service.CartService;
import com.vitakulina.apiEcommerce.service.business.exception.CartError;
import com.vitakulina.apiEcommerce.service.business.exception.CartException;
import com.vitakulina.apiEcommerce.service.business.exception.ProductError;
import com.vitakulina.apiEcommerce.service.business.exception.ProductException;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepo;
	private final ProductRepository productRepo;
	private final ProductInCartRepository productInCartRepo;
	
	public CartServiceImpl(CartRepository cartRepo, ProductRepository productRepo, 
			ProductInCartRepository productInCartRepo) {
		super();
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
		this.productInCartRepo = productInCartRepo;
	}


	@Override
	public CartDTO postNewCart(UserCartDTO userDetails) {
		validateUserDetails(userDetails);
		Cart cart = new Cart();
		cart.setFullName(userDetails.getFullName());
		cart.setEmail(userDetails.getEmail());
		cart.setCreationDate(LocalDate.now());
		cart.setTotal(BigDecimal.ZERO);
		cart.setStatus(CartStatus.NEW.getStatus());
		cart.setProductsInCart(new HashSet<ProductInCart>());
		
		cartRepo.save(cart);
		
		CartDTO cartDTO = new CartDTO();
		BeanUtils.copyProperties(cart, cartDTO);
		cartDTO.setProducts(new HashSet<ProductDTO>());
		
		return cartDTO;	
	}
	

	@Override
	public CartDTO postProductToCart(Long cartId, CartProductDTO cartProductDTO) {
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			
			validateQuantity(cartProductDTO);
			Product product = validateProduct(cartProductDTO);
			
			addToTotal(cart, product.getUnitPrice(), cartProductDTO.getQuantity());
			//TODO: restar de product stock el quantity agregado al cart
			
			ProductInCart prodInCart = new ProductInCart(); //TODO: como tratar el caso cuando se agrega un producto que ya esta en el cart pero ahora tiene otro precio?
			prodInCart.setCart(cart);
			prodInCart.setProduct(product);
			prodInCart.setQuantity(cartProductDTO.getQuantity());
			prodInCart.setUnitPrice(product.getUnitPrice());			
			productInCartRepo.save(prodInCart);	
			
			BeanUtils.copyProperties(cart, cartDTO);
			
			Set<ProductInCart> productsInCart = cart.getProductsInCart();
			Set<ProductDTO> productsDTO = getProductDTOSetInCart(productsInCart);					
			cartDTO.setProducts(productsDTO); 
		}else {
			throw new CartException(CartError.CART_NOT_PRESENT);
		}
		
		return cartDTO;							
	}
	


	@Override
	public CartDTO deleteProductFromCart(Long cartId, Long productId) {
		//TODO: definir en que situacion el cart estaria en modo PROCESSING para poder lanzar la excepcion
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			Set<ProductInCart> productsInCart = cart.getProductsInCart();
			Optional<ProductInCart> productToDelete = productsInCart.stream().filter(p -> p.getProduct().getId().equals(productId)).findFirst();
			if(productToDelete.isPresent()){
				//TODO: delete product from cart, update Cart Total and delete entries from ProductInCart table. Add back the stock to Product
			
				
				BeanUtils.copyProperties(cart, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(productsInCart));
			}else {
				throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
			}
			
			
		}else {
			throw new CartException(CartError.CART_NOT_PRESENT);
		}
		
		return cartDTO;
	}

	@Override
	public Set<ProductInCartDTO> getProductsInCart(Long cartId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartDTO getCart(Long cartId) {
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			BeanUtils.copyProperties(cart, cartDTO);

			cartDTO.setProducts(getProductDTOSetInCart(cart.getProductsInCart()));						
		}else {
			throw new CartException(CartError.CART_NOT_PRESENT);
		}				
		return cartDTO;
	}

	
	@Override
	public CartDTO postCheckoutCart(Long cartId) {
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			if(cart.getProductsInCart() != null && cart.getProductsInCart().size() > 0) {
				//Solo se va poder pasar a "READY" un cart que tenga productos agregados y un total mayor a cero, si no tiene se va lanzar una excepcion
				cart.setStatus(CartStatus.READY.getStatus());
				cartRepo.save(cart);
				
				BeanUtils.copyProperties(cart, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(cart.getProductsInCart()));
			}else {
				throw new CartException(CartError.CART_STATUS_NOT_ALLOW_CHECKOUT);
			}			
		}else {
			throw new CartException(CartError.CART_NOT_PRESENT);
		}
		return cartDTO;
	}
	
	
	private void validateUserDetails(UserCartDTO userDetails) {
		//validate presence of fullName and email
		if(userDetails.getFullName() == null || userDetails.getFullName().trim().isEmpty()) {
			throw new CartException(CartError.CART_FULLNAME_REQUIRED);
		}
		if(userDetails.getEmail() == null || userDetails.getEmail().trim().isEmpty()) {
			throw new CartException(CartError.CART_EMAIL_REQUIRED);
		}		
	}
	
	
	private Product validateProduct(CartProductDTO cartProductDTO) {
		if(cartProductDTO.getProductId() == null) {
			throw new ProductException(ProductError.PRODUCT_ID_REQUIRED);
		}
		
		Optional<Product> prodOpt = productRepo.findById(cartProductDTO.getProductId());
		if(prodOpt.isPresent()) {			
			return prodOpt.get();
		}else {
			throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
		}				
	}
	
	private Set<ProductDTO> getProductDTOSetInCart(Set<ProductInCart> productsInCart){
		System.out.println("List size : " + productsInCart.size());
		Set<ProductDTO> productsDTO = new HashSet<>();
		
		productsInCart.forEach((pr) ->{
			System.out.println("ProductsInCart id:  " + pr.getProduct().getId());
						
			Optional<Product> prodOpt = productRepo.findById(pr.getProduct().getId());
			if(prodOpt.isPresent()) {
				ProductDTO prodDTO = new ProductDTO();
				BeanUtils.copyProperties(prodOpt.get(), prodDTO);
				productsDTO.add(prodDTO);								
			}
		});		
		return productsDTO;		
	}
	
	
	private void validateQuantity(CartProductDTO cartProductDTO) {
		if(cartProductDTO.getQuantity() == null) {
			throw new CartException(CartError.PRODUCT_QUANTITY_REQUIRED);
		}else if(cartProductDTO.getQuantity() < 1){
			throw new CartException(CartError.PRODUCT_QUANTITY_INVALID);
		}else {
			Optional<Product> prodOpt = productRepo.findById(cartProductDTO.getProductId());
			if(prodOpt.isPresent()) {
				Product product = prodOpt.get();
				if(product.getStock() < cartProductDTO.getQuantity()) {
					throw new CartException(CartError.CART_INSUFFICIENT_PRODUCT_STOCK);
				}
			}
		}
	}
	
	private void addToTotal(Cart cart, BigDecimal unitPrice, Integer quantity) {
		BigDecimal productTotal = unitPrice.multiply(new BigDecimal(quantity));
		cart.setTotal(cart.getTotal().add(productTotal));
		cartRepo.save(cart); //updating cart with new total		
	}
	

	
}
