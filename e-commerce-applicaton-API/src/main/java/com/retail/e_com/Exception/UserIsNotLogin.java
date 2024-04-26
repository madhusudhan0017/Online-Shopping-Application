package com.retail.e_com.Exception;

public class UserIsNotLogin extends RuntimeException {

	private String message;

	public String getMessage() {
		return message;
	}

	public UserIsNotLogin(String message) {
		super();
		this.message = message;
	}

	
}
