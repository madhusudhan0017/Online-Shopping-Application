package com.retail.e_com.Exception;

public class InvalidCredentialsException extends RuntimeException{
	public String message;

	public String getMessage() {
		return message;
	}

	public InvalidCredentialsException(String message) {
		super();
		this.message = message;
	}
	

}
