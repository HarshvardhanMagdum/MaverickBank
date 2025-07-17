package com.springboot.bankDemo.exception;

public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String message;

	public ResourceNotFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
