package com.retail.e_com.serviceIMPL;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.retail.e_com.Exception.InvalidCredentialsException;
import com.retail.e_com.Exception.UserAllreadyLogoutException;
import com.retail.e_com.Exception.UserIsNotLogin;
import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.entity.AccessToken;
import com.retail.e_com.entity.Customer;
import com.retail.e_com.entity.RefreshToken;
import com.retail.e_com.entity.Seller;
import com.retail.e_com.entity.User;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.jwt.io.JWTService;
import com.retail.e_com.jwt.repository.AccessTokenRepo;
import com.retail.e_com.jwt.repository.RefreshTokenRepo;
import com.retail.e_com.mail_service.MailService;
import com.retail.e_com.repository.UserRepository;
import com.retail.e_com.requestDTO.AuthRequest;
import com.retail.e_com.requestDTO.OTPRequest;
import com.retail.e_com.requestDTO.UserRequest;
import com.retail.e_com.responseDTO.AuthResponse;
import com.retail.e_com.responseDTO.UserResponse;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.utility.MessageModel;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class AuthorServiceImpl implements AuthService {

	private UserRepository userRepository;
	private ResponseStructure<UserResponse> responseStructure;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private SimpleResponseStructure simpleResponseStructure;
	private MailService mailService;
	private AuthenticationManager authenticationManager; 
	private JWTService jwtService;
	private PasswordEncoder passwordEncoder;
	private AuthResponse authresponse;
	private AccessTokenRepo accesstokenrepo;
	private RefreshTokenRepo refreshtokenrepo;
	private ResponseStructure<AuthResponse> responseStruc;

	public AuthorServiceImpl(UserRepository userRepository, ResponseStructure<UserResponse> responseStructure,
			CacheStore<String> otpCache, CacheStore<User> userCache, SimpleResponseStructure simpleResponseStructure,
			MailService mailService, AuthenticationManager authenticationManager, JWTService jwtService,
			PasswordEncoder passwordEncoder, AuthResponse authresponse, AccessTokenRepo accesstokenrepo,
			RefreshTokenRepo refreshtokenrepo, ResponseStructure<AuthResponse> responseStruc) {
		super();
		this.userRepository = userRepository;
		this.responseStructure = responseStructure;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.simpleResponseStructure = simpleResponseStructure;
		this.mailService = mailService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authresponse = authresponse;
		this.accesstokenrepo = accesstokenrepo;
		this.refreshtokenrepo = refreshtokenrepo;
		this.responseStruc = responseStruc;
	}

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiry;

	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiry;

	@Override
	public ResponseEntity<SimpleResponseStructure> registerUser( UserRequest userRequest) {
		if(userRepository.existsByEmail(userRequest.getUserEmail()))throw new RuntimeException("user is allready present....");
		User user=mapToChildEntity(userRequest);
		String otp =generateOTP();

		System.out.println(user.getemail()+"in side register");
		otpCache.add(user.getemail(), otp);
		userCache.add(user.getemail(), user);
		try {
			sendOTP(user,otp);
		}catch (MessagingException e) {
			e.printStackTrace();
		}

		System.err.println(otp);

		return ResponseEntity.ok(simpleResponseStructure
				.setStatus(HttpStatus.ACCEPTED.value())
				.setMessage("Verify OTP sent thrrough mail to complete registration! "+ " expires in 1 minute"));
	}

	private void sendOTP(User user, String otp) throws MessagingException {
		// Construct the email body
		String emailBody = "<P>hi,<br>"
				+ "Thanks for your interest in E-com. "
				+ "Please verify your mail Id using the OTP given below,</p>"
				+ "<br>"
				+ "<h1>" + otp + "</h1>"
				+ "</br>"
				+ "<p>Please ignore it if it's not you.</p>"
				+ "<br>"
				+ "With best regards,<br>"
				+ "<h3>E-Com</h3>";

		// Construct the MessageModel directly
		MessageModel model = new MessageModel();
		model.setTo(user.getemail());
		model.setSubject("Verify your OTP");
		model.setText(emailBody);

		// Send the email using mailService
		mailService.sendMailMessage(model);

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOtp(OTPRequest otpRequest){
		System.out.println(otpRequest.getEmail());
		System.out.println(otpCache.get(otpRequest.getEmail()));
		if(otpCache.get(otpRequest.getEmail())==null)throw new RuntimeException();//otp expired
		if(!otpCache.get(otpRequest.getEmail()).equals(otpRequest.getOtp())) throw new RuntimeException();//invalid Otp

		User user=userCache.get(otpRequest.getEmail());
		System.out.println(user);
		if(user==null) 
			throw new RuntimeException();//registration session expired

		user.setEmailVerified(true);
		user=userRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(responseStructure.setBody(mapToUserResponse(user))
						.setStatus(HttpStatus.CREATED.value())
						.setMessage("OTP verification successfull"));

	}

	private UserResponse mapToUserResponse(User user) {
		return new UserResponse(user.getUserId(), user.getDisplayName(), user.getUserName(), user.getemail(), user.getUserRole());
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(000000,999999));
	}


	private <T extends User>T mapToChildEntity(UserRequest userRequest) {
		UserRole role =userRequest.getUserRole();

		User user;
		switch(role) {
		case SELLER -> user=new Seller();
		case CUSTOMER->user=new Customer();

		default->throw new RuntimeException();
		}

		user.setDisplayName(userRequest.getUserName());
		user.setUserName(userRequest.getUserEmail().split("@gmail.com")[0]);
		user.setemail(userRequest.getUserEmail());
		user.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));
		user.setUserRole(userRequest.getUserRole());
		user.setEmailVerified(false);
		return (T)user;
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest) {
		String userName = authRequest.getUserEmail().split("@gmail.com")[ 0];
		Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,authRequest.getUserPassword()));

		if(!authentication.isAuthenticated()) throw new InvalidCredentialsException("Authentication Failed ");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpHeaders headers = new HttpHeaders();

		userRepository.findByUserName(userName).ifPresent(user -> {
			//GENERATE ACCESS AND REFRESH TOKEN
			AccessToken accessToken =	generateAccessToken(user, headers, new AccessToken());
			RefreshToken refreshToken = generateRefreshToken(user, headers, new RefreshToken());

			

			accesstokenrepo.save(accessToken);
			refreshtokenrepo.save(refreshToken);

		});
		return ResponseEntity.ok().headers(headers).body(responseStruc.setMessage("")
				.setStatus(HttpStatus.OK.value())
				.setBody(mapToAuthResponse(userName,accessExpiry,refreshExpiry)));
	}
	private AuthResponse mapToAuthResponse(String username, long accessExpiration, long refreshExpiration) {
		User user = userRepository.findByUserName(username).get();

		authresponse.setUserId(user.getUserId());
		authresponse.setUserName(user.getUserName());
		authresponse.setUserRole(user.getUserRole());
		authresponse.setAcessExpiration(accessExpiration);
		authresponse.setRefreshExpiration(LocalDateTime.now());

		return authresponse;
	}

	private RefreshToken generateRefreshToken (User user, HttpHeaders headers, RefreshToken refreshToken) {
		String token = jwtService.generateRefreshToken(user.getUserName(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", token, refreshExpiry));
		//store the token to the database
		refreshToken.setToken(token);
		refreshToken.setTokenExpiration(LocalDateTime.now());
		refreshToken.setBlocked(false);

		return refreshToken;
	}

	private AccessToken generateAccessToken(User user, HttpHeaders headers, AccessToken accessToken) {
		String token = jwtService.generateAccessToken(user.getUserName(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("at", token, accessExpiry));

		accessToken.setToken(token);
		accessToken.setTokenExpiration(LocalDateTime.now());
		accessToken.setBlocked(false);

		return accessToken;
	}

	private String configureCookie(String userName,String value, long maxAge) {
		return ResponseCookie.from(userName, value)
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofMillis(maxAge))
				.sameSite("Lax")
				//IF SITE = NONE-->  ACCEPTS COOKIES ONLY HTTPS FROM ONLY ORIGIN 
				//          LAX  -->  WORK WITH HTTP
				//          STRICT--> ACCEPT COOKIES ONLY FROM SAME ORIGIN
				.build().toString();

		//ADD AS COOKIE TO THE RESPONSE
		//GENERATE AUTH RESPONSE AND RETURN 
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> logout(String accessToken, String refreshToken) {
	
		if(accessToken==null || refreshToken==null ) 
			throw new UserIsNotLogin("Please LogIn");

		HttpHeaders headers = new HttpHeaders();
		
		accesstokenrepo.findByToken(accessToken).ifPresent(access ->{

			 refreshtokenrepo.findByToken(refreshToken).ifPresent(refresh ->{
				
				 access.setBlocked(true);
				accesstokenrepo.save(access);
				refresh.setBlocked(true);
				refreshtokenrepo.save(refresh);

				removeAccess("at",headers);
				removeAccess("rt",headers);

			});
		});
		return ResponseEntity.ok().headers(headers)
				.body(simpleResponseStructure.setMessage("LogOut Sucessfully...")
						.setStatus(HttpStatus.OK.value()));
	}


	private void removeAccess(String value, HttpHeaders headers) {
		headers.add(HttpHeaders.SET_COOKIE, removeCookie(value));
	}
	private String removeCookie(String name) {
		return ResponseCookie.from(name,"").domain("localhost").path("/").httpOnly(true).secure(false).maxAge(0).sameSite("Lax").build().toString();
	}
}