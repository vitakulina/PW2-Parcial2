package com.vitakulina.apiEcommerce.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	

	//TODO: no permitir agregar productos a un Cart que esta en status CHECKOUT (READY)
	//TODO: un cart que ya esta en checkout (READY) no se puede poner de vuelta en checkout ni agregarle productos ni sacarle productos
	//TODO: implementar para Products en delete la misma logica que en put product sin id, que sea una variable de path opcional y que lance una excepcion de que falta el id
	//TODO: el descuento de stock en el producto se va hacer en el checkout de los carritos, no antes. En el proyecto lo vamos a hacer en batch. Se van a tomar todos los carritos en ready y ahi se van a procesar, se va descontar el stock y se pone en Finalizado el carrito.
	//TODO: no permitir crear dos carritos con el mismo mail -> para Testing unitario. Se prueba del servicio para atras.
	
	
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
			
			ProductInCart prodInCart = new ProductInCart(); 
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
		//FIXME: internal server error si un producto fue agregado mas de una vez al carrito
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
					productInCartRepo.delete(pr);
					System.out.println("--- ELIMINO ---");
					
				});
				System.out.println("--- salio del for -----------------");
				//cartRepo.save(cart);
				BeanUtils.copyProperties(cart, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(cart.getProductsInCart()));
				
			}else {
				throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
			}
			
			
			
			
			/*
			Optional<ProductInCart> productToDeleteOpt = productsInCart.stream().filter(p -> p.getProduct().getId().equals(productId)).findFirst();
			if(productToDeleteOpt.isPresent()){
				//TODO: delete product from cart, update Cart Total and delete entries from ProductInCart table. Add back the stock to Product
				
			
				
				
				
				BeanUtils.copyProperties(cart, cartDTO);
				cartDTO.setProducts(getProductDTOSetInCart(productsInCart));
			}else {
				throw new ProductException(ProductError.PRODUCT_NOT_PRESENT);
			}*/
			
			
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
	

	private void deductFromTotal(Cart cart, BigDecimal unitPrice, Integer quantity) {
		BigDecimal productTotal = unitPrice.multiply(new BigDecimal(quantity));
		cart.setTotal(cart.getTotal().subtract(productTotal));
		cartRepo.save(cart);
		
	}


	
	
}
