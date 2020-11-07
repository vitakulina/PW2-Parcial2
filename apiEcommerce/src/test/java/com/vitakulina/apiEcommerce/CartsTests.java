package com.vitakulina.apiEcommerce;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.vitakulina.apiEcommerce.model.dto.CartDTO;
import com.vitakulina.apiEcommerce.model.dto.UserCartDTO;
import com.vitakulina.apiEcommerce.repository.CartRepository;
import com.vitakulina.apiEcommerce.service.CartService;
import com.vitakulina.apiEcommerce.service.business.exception.CartException;


@SpringBootTest
@Transactional
public class CartsTests {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Test
	@DisplayName("╯°□°）╯")
	public void cartCheckoutWithInvalidIdThrowException() {
		Assertions.assertThrows(CartException.class, () -> {
			cartService.postCheckoutCart(77777L);
		});
		
	}
	
	@Test
	public void whenCartIsRequestWithValidStatusThenRespondOk() {
		//TODO: hacer la implementacion real de getCartByStatus y cambiar el assertTrue de acuerdo a lo que hay en la DB
		List<CartDTO> result = cartService.getCartByStatus("NEW");
		Assertions.assertTrue(result.isEmpty()); //testeamos que no haya ningun carrito new (en este caso esta hardcodeado el servicio a devolver null)
		
	}
	
	
	@Test
	public void whenCartIsRequestWithInvalidStatusThenThrowException() {
		//TODO: implementarlo de verdad y esparar la excepcion correspondiente (todavia no esta creado ese error)
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
		Assertions.assertTrue(result.size() == 2); //TODO: implementar de verdad
		
	}
	

}

