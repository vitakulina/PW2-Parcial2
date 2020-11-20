package com.vitakulina.apiEcommerce.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.vitakulina.apiEcommerce.service.SendEmailService;

@Service
public class SendEmailServiceImpl implements SendEmailService {

	private final static String SMTP_SERVER_HOST = "localhost";
	private final static String SMTP_SERVER_PORT = "25";
	
	@Override
	public boolean sendEmail(String from, String to, String subject, String body) {
		Properties properties = System.getProperties();
	    properties.setProperty("mail.smtp.host", SMTP_SERVER_HOST);
	    properties.setProperty("mail.smtp.port", SMTP_SERVER_PORT);

	    Session session = Session.getDefaultInstance(properties);
		
	    try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);
	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));
	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         // Set Subject: header field
	         message.setSubject(subject);
	         // Now set the actual message
	         message.setText(body);
	         // Send message
	         Transport.send(message);
	         System.out.println("Message sent successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	         return false;
	      }

		return true;
	}

}
