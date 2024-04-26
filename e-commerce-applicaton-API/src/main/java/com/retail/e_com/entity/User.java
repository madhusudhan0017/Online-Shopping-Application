package com.retail.e_com.entity;

import com.retail.e_com.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String displayName;
	private String userName;
	private String email;
	private String userPassword;
	private UserRole userRole;
	private boolean emailVerified;
	
	public int getUserId() {
		return userId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getUserName() {
		return userName;
	}
	public String getemail() {
		return email;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public boolean isEmailVerified() {
		return emailVerified;
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
	public void setemail(String email) {
		this.email = email;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	// Lifecycle callback method to generate username before entity is persisted
    @PrePersist
    public void generateUsername() {
        // Extract username from email (assuming email format is username@example.com)
        String[] parts = email.split("@");
        if (parts.length > 0) {
            userName = parts[0];
        } else {
            // If email format is not as expected, you might handle it differently
            throw new IllegalArgumentException("Invalid email format");
        }
    }

}
