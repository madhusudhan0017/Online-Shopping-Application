package com.retail.e_com.requestDTO;

import com.retail.e_com.enums.UserRole;

public class UserRequest {
	
	private String userName;
	private String userPassword;
	private String userEmail;
	private UserRole userRole;
	
	public String getUserName() {
		return userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	
	

}
