package com.retail.e_com.responseDTO;

import java.time.LocalDateTime;

import org.springframework.context.support.BeanDefinitionDsl.Role;
import org.springframework.stereotype.Component;

import com.retail.e_com.enums.UserRole;

@Component
public class AuthResponse {

	private int userId;
	private String userName;
	private UserRole userRole;
	private long acessExpiration;
	private LocalDateTime refreshExpiration;
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public long getAcessExpiration() {
		return acessExpiration;
	}
	public void setAcessExpiration(long acessExpiration) {
		this.acessExpiration = acessExpiration;
	}
	public LocalDateTime getRefreshExpiration() {
		return refreshExpiration;
	}
	public void setRefreshExpiration(LocalDateTime refreshExpiry2) {
		this.refreshExpiration = refreshExpiry2;
	}
	
	
}
