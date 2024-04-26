package com.retail.e_com.Exception;

public class UserAllreadyLogoutException extends RuntimeException {

	private String message;
	
	
	public String getMessage() {
		return message;
	}

	public UserAllreadyLogoutException(String message) {
		super();
		this.message = message;
	}


	

	

}
