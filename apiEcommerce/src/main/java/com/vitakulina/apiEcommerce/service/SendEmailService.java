package com.vitakulina.apiEcommerce.service;

import org.springframework.stereotype.Service;

@Service
public interface SendEmailService {
	public boolean sendEmail(String from, String to, String subject, String body);

}
