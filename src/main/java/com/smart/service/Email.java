package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class Email {

	public boolean sendEmail(String message, String subject, String to) {
		boolean flag = false;
		
		String from = "rinkimittal2006@gmail.com";
		
		String host = "smtp.gmail.com";
		
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("rinkimittal2006@gmail.com", "rjhp lrxo vmnn pxkt");
			}
			
		});
		
		MimeMessage m = new MimeMessage(session);
		try {
		m.setFrom(from);
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		m.setSubject(subject);
		m.setText(message);
		
		Transport.send(m);
		flag = true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
		
	}
}
