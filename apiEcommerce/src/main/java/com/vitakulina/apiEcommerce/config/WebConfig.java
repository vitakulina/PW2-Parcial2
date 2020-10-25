package com.vitakulina.apiEcommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport{
	
	//The below enables CORS from all origins, to be used in the entire api for all methods
	//this makes the @CrossOrigin annotation redundant (and thus not used on the JwtAuthenticationController)
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("*");
	}

}
