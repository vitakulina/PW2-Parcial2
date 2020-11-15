package com.vitakulina.apiEcommerce.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.ProductInCart;
import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartDTOWithHttpStatus;
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

	


	//TODO: el descuento de stock en el producto se va hacer en el checkout de los carritos, no antes. En el proyecto lo vamos a hacer en batch. Se van a tomar todos los carritos en ready y ahi se van a procesar, se va descontar el stock y se pone en Finalizado el carrito.
	//TODO: no permitir crear dos carritos (con estado new) con el mismo mail
	//TODO: cambiar como funciona agregar productos -> no generar nuevos ProductInCart si se agrega el mismo producto, solo debe actualizar la cantidad en la db de ProdInCart
	
	
	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepo.findAll();
		List<CartDTO> cartsDTO = new ArrayList<>();
		if(carts != null) {
			
			carts.forEach(c ->{
				CartDTO cartDTO = new CartDTO();
				BeanUtils.copyProperties(c, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(c.getProductsInCart()));
				cartsDTO.add(cartDTO);
			});
			
			
		}else {
			throw new CartException(CartError.NO_CARTS_AVAILABLE);
		}
		
		return cartsDTO;
	}
	
	@Override
	@Transactional
	public CartDTOWithHttpStatus postNewCart(UserCartDTO userDetails) {
		validateUserDetails(userDetails);		
		Cart cart = new Cart();
		CartDTOWithHttpStatus cartWithStatus = new CartDTOWithHttpStatus();
		//chequea si ya hay un cart en New status para el mismo mail:
		List<Cart> cartExistent = cartRepo.findByEmailAndStatusAllIgnoreCase(userDetails.getEmail(), "NEW");
		if(cartExistent != null && cartExistent.size() > 0) {
			cart = cartExistent.get(0); // TODO: si ya existe, deberiamos devolver status 200 en vez de 201
			cartWithStatus.setHttpStatus(HttpStatus.OK);
		}else {			
			cart.setFullName(userDetails.getFullName());
			cart.setEmail(userDetails.getEmail());
			cart.setCreationDate(LocalDate.now());
			cart.setTotal(BigDecimal.ZERO);
			cart.setStatus(CartStatus.NEW.getStatus());
			cart.setProductsInCart(new HashSet<ProductInCart>());
			
			cartRepo.save(cart);
			cartWithStatus.setHttpStatus(HttpStatus.CREATED);
		}
				
		CartDTO cartDTO = new CartDTO();
		BeanUtils.copyProperties(cart, cartDTO);
		cartDTO.setProducts(new HashSet<ProductDTO>());
		cartWithStatus.setCartDTO(cartDTO);
		
		return cartWithStatus;	
	}
	

	@Override
	public CartDTO postProductToCart(Long cartId, CartProductDTO cartProductDTO) {
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			
			if (!cart.getStatus().equalsIgnoreCase("NEW")){
				//si el cart no esta en estado "new" no se le permite agregar productos
				throw new CartException(CartError.CART_STATUS_NOT_ALLOW_CHECKOUT); 
			}
			validateQuantity(cartProductDTO);
			Product product = validateProduct(cartProductDTO);
			
			addToTotal(cart, product.getUnitPrice(), cartProductDTO.getQuantity());
			
			ProductInCart prodInCart = new ProductInCart(); 
			prodInCart.setCart(cart);
			prodInCart.setProduct(product);
			prodInCart.setQuantity(cartProductDTO.getQuantity());
			prodInCart.setUnitPrice(product.getUnitPrice());
			prodInCart.setDescription(product.getDescription());
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
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		CartDTO cartDTO = new CartDTO();
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			if (cart.getStatus().equals("READY")){
				throw new CartException(CartError.CART_PROCESSING_NOT_ALLOW_DELETION);
			}
			Set<ProductInCart> productsInCart = cart.getProductsInCart();
			Set<ProductInCart> productsToDelete = productsInCart.stream()
					.filter(p -> p.getProduct().getId().equals(productId)).collect(Collectors.toSet());
			
			System.out.println("Amount of products to delete: " + productsToDelete.size());
			if(productsToDelete != null && productsToDelete.size() > 0) {
				
				productsToDelete.forEach(pr -> {
					deductFromTotal(cart, pr.getUnitPrice(), pr.getQuantity());
					cart.removeProduct(pr);
					productInCartRepo.delete(pr);
					
				});
				BeanUtils.copyProperties(cart, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(cart.getProductsInCart()));
				
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
		Set<ProductInCartDTO> productsInCartDTO = new HashSet<>();
		Optional<Cart> cartOpt = cartRepo.findById(cartId);
		if(cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			Set<ProductInCart> productsInCart = cart.getProductsInCart();
			productsInCart.forEach(pr ->{
				ProductInCartDTO prDTO = new ProductInCartDTO();
				BeanUtils.copyProperties(pr, prDTO); //esto no copia el product id ya que las propiedades se llaman distinto 
				prDTO.setProductId(pr.getProduct().getId());
				productsInCartDTO.add(prDTO);
			});
		}else {
			throw new CartException(CartError.CART_NOT_PRESENT);
		}
		
		return productsInCartDTO;
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
			if(cart.getProductsInCart() != null && cart.getProductsInCart().size() > 0 && cart.getStatus().equalsIgnoreCase("NEW")) {
				//Solo se va poder pasar a "READY" un cart que tenga productos agregados si no tiene se va lanzar una excepcion y también si no esta en el status "new"
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
		//TODO: validar email, que tenga formato valido
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
		Set<ProductDTO> productsDTO = new HashSet<>();
		
		productsInCart.forEach((pr) ->{						
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
		} else {  
			Optional<Product> prodOpt = productRepo.findById(cartProductDTO.getProductId());
			if(prodOpt.isPresent()) {
				Product product = prodOpt.get();
				if(product.getStock() < cartProductDTO.getQuantity()) {
					throw new ProductException(ProductError.PRODUCT_STOCK_INSUFFICIENT);
				}
			}
		} 
	}
	
	private void addToTotal(Cart cart, BigDecimal unitPrice, Integer quantity) {
		BigDecimal productTotal = unitPrice.multiply(new BigDecimal(quantity));
		cart.setTotal(cart.getTotal().add(productTotal));
		cartRepo.save(cart); //updating cart with new total		
	}
	

	private void deductFromTotal(Cart cart, BigDecimal unitPrice, Integer quantity) {
		BigDecimal productTotal = unitPrice.multiply(new BigDecimal(quantity));
		cart.setTotal(cart.getTotal().subtract(productTotal));
		cartRepo.save(cart);
		
	}



	@Override
	public List<CartDTO> getCartsByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<CartDTO> getCartsByStatus(Optional<String> statusOpt) {
		//check if status is available and if so, check if it's valid (available in the enum)
		if(statusOpt.isPresent()) {
			List<CartDTO> cartsDTO = new ArrayList<CartDTO>();
			String status = statusOpt.get();
			if(EnumUtils.isValidEnum(CartStatus.class, status.toUpperCase())) {
				//obtener carts con el status valido
				List<Cart>carts = cartRepo.findByStatusIgnoreCase(status);
				if(carts != null) {
					carts.forEach(c ->{
						CartDTO cartDTO = new CartDTO();
						BeanUtils.copyProperties(c, cartDTO);
						cartDTO.setProducts(getProductDTOSetInCart(c.getProductsInCart()));
						cartsDTO.add(cartDTO);
					});
				}
				
				return cartsDTO;
			}else {
				throw new CartException(CartError.CART_STATUS_NOT_SUPPORTED);
			}
			
		}else {
			return getAllCarts();
		}		
	}


	
	
}
