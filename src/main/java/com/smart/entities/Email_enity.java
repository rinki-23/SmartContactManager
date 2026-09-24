package com.smart.entities;

public class Email_enity {

	private String message;
	private String subject;
	private String to;
	public Email_enity(String message, String subject, String to) {
		super();
		this.message = message;
		this.subject = subject;
		this.to = to;
	}
	public Email_enity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "Email_enity [message=" + message + ", subject=" + subject + ", to=" + to + "]";
	}
	
	
	
}
