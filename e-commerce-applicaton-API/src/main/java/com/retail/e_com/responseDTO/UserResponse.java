package com.retail.e_com.responseDTO;

import com.retail.e_com.enums.UserRole;

public class UserResponse {

	private int userId;
	private String displayName;
	private String userName;
	private String userEmail;
	private UserRole userRole;
	
	public UserResponse(int userId, String userName, String displayName, String userEmail, UserRole userRole) {
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	
}
