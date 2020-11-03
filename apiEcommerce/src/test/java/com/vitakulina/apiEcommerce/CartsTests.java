package com.vitakulina.apiEcommerce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vitakulina.apiEcommerce.repository.CartRepository;
import com.vitakulina.apiEcommerce.service.CartService;
import com.vitakulina.apiEcommerce.service.business.exception.CartException;


@SpringBootTest
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
	

}

