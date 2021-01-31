package com.vitakulina.apiEcommerce;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.CartDTOWithHttpStatus;
import com.vitakulina.apiEcommerce.model.dto.CartProductDTO;
import com.vitakulina.apiEcommerce.model.dto.ProductDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.repository.CartRepository;
import com.vitakulina.apiEcommerce.service.CartService;
import com.vitakulina.apiEcommerce.service.ProductService;
import com.vitakulina.apiEcommerce.service.business.exception.CartException;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
public class CartsTests {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private  CartRepository cartRepo;
	
	@Autowired 
	private ProductService productService;
	
	private static List<CartDTO> allCarts;
	
	@BeforeEach
	public void init() {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setDescription("Dune");
		productDTO.setStock(20);
		productDTO.setUnitPrice(new BigDecimal(300));
		productDTO = productService.post(productDTO);
		Long prodId = productDTO.getId();
		
		CartDTOWithHttpStatus cartDTO = cartService.postNewCart(new UserCartDTO("Vita Kulina", "vita@mail.com")); 
		Long idCart1 = cartDTO.getCartDTO().getId();
		cartService.postProductToCart(idCart1, new CartProductDTO(prodId, 2));
		cartService.postCheckoutCart(idCart1);
		
		CartDTOWithHttpStatus cartDTO2 = cartService.postNewCart(new UserCartDTO("Vita Kulina", "vita@mail.com"));
		Long idCart2 = cartDTO2.getCartDTO().getId();
		cartService.postProductToCart(idCart2, new CartProductDTO(prodId, 1));
		
		setAllCarts(cartService.getAllCarts());
	}
	
	
	@Test
	@DisplayName("╯°□°）╯")
	public void cartCheckoutWithInvalidIdThrowException() {
		Assertions.assertThrows(CartException.class, () -> {
			cartService.postCheckoutCart(77777L);
		});
		
	}
	
	@Test
	@DisplayName("Test new cart de Vita")
	void testCart() {
		List<Cart> cartVita = cartRepo.findByEmailAndStatusAllIgnoreCase("vita@mail.com", "New");
		Assertions.assertEquals(1, cartVita.size());
		Assertions.assertNotEquals("Ready", cartVita.get(0).getStatus());
	}
	
	
	@Test
	@DisplayName("Que existan 2 carritos")
	void testOnlyTwoCartsShouldExist() {
		Assertions.assertEquals(2, getAllCarts().size());
	}
	
	
	@Test
	@DisplayName("Que exista solo un carrito Ready")
	void testOnlyOneProcessedCart() {
		List <CartDTO> carts = cartService.getCartsByStatus(Optional.of("Ready"));
		System.out.println(carts.size());
		Assertions.assertEquals(1, carts.size());
	}
	
	@Test
	@DisplayName("Que exista solo un carrito New")
	void testOnlyOneNewCart() {

		Assertions.assertEquals(1, cartService.getCartsByStatus(Optional.of("New")).size());
	}
	
	@Test
	@DisplayName("Que no exista ningun carrito con Failed")
	void testNoFailedCart() {

		Assertions.assertEquals(0, cartService.getCartsByStatus(Optional.of("Failed")).size());
	}
	
	@Test
	@DisplayName("Que no exista ningun carrito con Processed")
	void testNoProcessedCart() {

		Assertions.assertEquals(0, cartService.getCartsByStatus(Optional.of("Processed")).size());
	}
	
	@Test
	@DisplayName("Que lance una excepcion si el status no es el esperado - Nuevo")
	void testExceptionWhenUsingInvalidStatus() {
		Assertions.assertThrows(CartException.class, () -> {
			cartService.getCartsByStatus(Optional.of("Nuevo"));
		});

	}


	public List<CartDTO> getAllCarts() {
		return allCarts;
	}


	public void setAllCarts(List<CartDTO> allCarts) {
		CartsTests.allCarts = allCarts;
	}
	
	
	
	/*
	
	@Test
	public void whenCartIsRequestWithValidStatusThenRespondOk() {
		
		List<CartDTO> result = cartService.getCartByStatus("NEW");
		Assertions.assertTrue(result.isEmpty()); //testeamos que no haya ningun carrito new (en este caso esta hardcodeado el servicio a devolver null)
		
	}
	
	
	@Test
	public void whenCartIsRequestWithInvalidStatusThenThrowException() {
		
		List<CartDTO> result = cartService.getCartByStatus("NUEVO");
		Assertions.assertTrue(result.isEmpty()); //testeamos que no haya ningun carrito new (en este caso esta hardcodeado el servicio a devolver null)
		
	}
	
	@Test
	public void whenCartIsRequestWithEmptyStatusReturnAll() {
		//insertando mock data para poder correr las pruebas, para que eso funcione necesita tener la anotacion @Transactional:
		//para no interferir con la db real, hay que configurar una separada para los tests y va estar en memoria, sin guardar en archivo. Para eso se crea una carpeta de resources en test con las app properties para test. Tiene que ir a la altura de src/test
		UserCartDTO details = new UserCartDTO("vita kulina", "vita@email.com");
		cartService.postNewCart(details);
		
		UserCartDTO details2 = new UserCartDTO("test test", "test@email.com");
		cartService.postNewCart(details2);
		
		
		String status = null;
		List<CartDTO> result = cartService.getCartByStatus(status);
		Assertions.assertTrue(result.size() == 2);
		
	}
	*/
	

}

