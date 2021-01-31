package com.vitakulina.apiEcommerce;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vitakulina.apiEcommerce.model.Cart;
import com.vitakulina.apiEcommerce.model.Product;
import com.vitakulina.apiEcommerce.model.ProductInCart;
import com.vitakulina.apiEcommerce.repository.CartRepository;
import com.vitakulina.apiEcommerce.repository.ProductInCartRepository;
import com.vitakulina.apiEcommerce.repository.ProductRepository;



@SpringBootTest
public class CartsParallelismTest {
	
	@Autowired
	private  CartRepository cartRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	
	public CartsParallelismTest() {
		
	}
	
	@Test
	@DisplayName("Test processing all carts without parallelism")
	public void testAllCarts() {
		//1-traerlos filtrados por estado
		//2-traerlos ordenados
		
		List<Cart> carts = cartRepo.findAll();
		LocalDateTime start = LocalDateTime.now();
		System.out.println("INICIO: " + start.toString());
		
		for (Cart cart : carts) {
			System.out.println("INICIO de procesamiento carrito: " + cart.getId() + " time:" + LocalDateTime.now());
			
			
			 //this gets a set of ProductInCart entity, not product
			cart.getProductsInCart()
				.forEach(prodInCart -> {
					System.out.println("procesamiento producto: " + prodInCart.getProduct().getId() + " time:" + LocalDateTime.now());
					// logica de proceso del producto
					productRepo.findAll();
					System.out.println("Num of products in repo: " + productRepo.findAll().size());
					productRepo.findByDescriptionContainsIgnoreCase("algo"); //query random que lleve algo mas de complejidad, solo para testear los tiempos
					System.out.println("Num of products in repo with pelota in name: " + productRepo.findByDescriptionContainsIgnoreCase("pelota").size());
					System.out.println(prodInCart.getProduct().getDescription());
				});  
						
			System.out.println("FIN de procesamiento carrito: " + cart.getId() + " time:" + LocalDateTime.now());
		}
		
		LocalDateTime end = LocalDateTime.now();
		System.out.println("FIN: " + end.toString());

		long seconds = start.until(LocalDateTime.now(), ChronoUnit.MILLIS );
		System.out.println("DURACION TOTAL NO PARALLEL: " + seconds);
		
	}
	
	@Test
	@DisplayName("Test processing all carts with parallel streams")
	public void testAllCartsInParallel() {
		//1-traerlos filtrados por estado
		//2-traerlos ordenados
		List<Cart> carts = cartRepo.findAll();
		LocalDateTime start = LocalDateTime.now();
		System.out.println("INICIO: " + start);
		
		for (Cart cart : carts) {
			System.out.println("INICIO de procesamiento carrito: " + cart.getId() + " time:" + LocalDateTime.now());
			
			cart.getProductsInCart().parallelStream()
				.forEach(product -> {
					System.out.println("procesamiento producto: " + product.getProduct().getId() + " time:" + LocalDateTime.now());
					// logica de proceso del producto
					productRepo.findAll();
					System.out.println("Num of products in repo: " + productRepo.findAll().size());
					productRepo.findByDescriptionContainsIgnoreCase("algo");
					System.out.println("Num of products in repo with pelota in name: " + productRepo.findByDescriptionContainsIgnoreCase("pelota").size());
					System.out.println(product.getProduct().getDescription());
				});
			System.out.println("FIN de procesamiento carrito: " + cart.getId() + " time:" + LocalDateTime.now());
		}
		
		System.out.println("FIN: " + LocalDateTime.now());
		
		long seconds = start.until(LocalDateTime.now(), ChronoUnit.MILLIS );
		System.out.println("DURACION TOTAL PARALLEL: " + seconds);
	}
	

}
