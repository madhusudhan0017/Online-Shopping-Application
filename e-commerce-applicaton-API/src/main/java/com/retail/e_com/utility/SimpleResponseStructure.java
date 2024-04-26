package com.retail.e_com.utility;

import org.springframework.stereotype.Component;

@Component
public class SimpleResponseStructure {
	private String message;
	private int status;


	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}


	public SimpleResponseStructure setMessage(String message) {
		this.message = message;
		return this;
	}


	public SimpleResponseStructure setStatus(int status) {
		this.status = status;
		return this;
	}


}
