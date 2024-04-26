package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.requestDTO.AuthRequest;
import com.retail.e_com.requestDTO.OTPRequest;
import com.retail.e_com.requestDTO.UserRequest;
import com.retail.e_com.responseDTO.AuthResponse;
import com.retail.e_com.responseDTO.UserResponse;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

public interface AuthService {

	ResponseEntity<SimpleResponseStructure> registerUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OTPRequest otpRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest);
	
	ResponseEntity<SimpleResponseStructure> logout(String accessToken, String refreshToken);

}
