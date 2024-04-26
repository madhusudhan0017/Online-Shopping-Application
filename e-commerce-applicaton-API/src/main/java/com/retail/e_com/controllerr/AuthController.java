package com.retail.e_com.controllerr;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.jwt.io.JWTService;
import com.retail.e_com.requestDTO.AuthRequest;
import com.retail.e_com.requestDTO.OTPRequest;
import com.retail.e_com.requestDTO.UserRequest;
import com.retail.e_com.responseDTO.AuthResponse;
import com.retail.e_com.responseDTO.UserResponse;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins= "http://localhost:5173")
public class AuthController {
	
	private AuthService authService;

	private JWTService jwtservice;
	

	public AuthController(AuthService authService, JWTService jwtservice) {
		super();
		this.authService = authService;
		this.jwtservice = jwtservice;
	}
	
	@PostMapping("/register")
	public ResponseEntity<SimpleResponseStructure> registerUser(@RequestBody UserRequest userRequest){
		return authService.registerUser(userRequest);
	}

	@PostMapping("/verify-email")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(@RequestBody OTPRequest otpRequest)
	{
		return authService.verifyOtp(otpRequest);
	}
	

	@GetMapping("/test")
	public String test( ) {
		return jwtservice.generateAccessToken("Madhusudhan","SELLER");
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> userlogin(@RequestBody AuthRequest authRequest)
	{
		return authService.login(authRequest);
	}
	
	
	@PostMapping("/logout")
	public ResponseEntity<SimpleResponseStructure> userlogout(@CookieValue ("at") String accessToken, @CookieValue ("rt") String refreshToken)
	{
		return authService.logout(accessToken,refreshToken);
	}
	
}

