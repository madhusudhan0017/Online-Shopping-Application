package com.retail.e_com.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.e_com.Exception.ConnectException;
import com.retail.e_com.Exception.EmailAllreadyPresentException;
import com.retail.e_com.Exception.InvalidCredentialsException;
import com.retail.e_com.Exception.InvalidEmailException;
import com.retail.e_com.Exception.InvalidOTPException;
import com.retail.e_com.Exception.MailConnectException;
import com.retail.e_com.Exception.OTPExpiryException;
import com.retail.e_com.Exception.RegistrationExpiryException;
import com.retail.e_com.Exception.RoleNotSpecifyException;
import com.retail.e_com.Exception.UserAllreadyPresentException;
import com.retail.e_com.Exception.UserIsNotLogin;
import com.retail.e_com.utility.ErrorStructure;

import lombok.AllArgsConstructor;
@RestControllerAdvice

public class AuthCustomExceptionHandler {
	
	private ErrorStructure<String> errorStructure;
	
	
	public AuthCustomExceptionHandler(ErrorStructure<String> errorStructure) {
		super();
		this.errorStructure = errorStructure;
	}

    @ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlingUserAllreadyPresentException(UserAllreadyPresentException e) {
		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
				.setMessage("User is allready present please enter unique username").setRootcause(e.getMessage()));	
	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingEmailAllreadyPresentException(EmailAllreadyPresentException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Email is allready present please enter unique EmailName").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingRoleNotSpecifyException(RoleNotSpecifyException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Role is not present please enter role to the user").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingInvalidOTPExceptionException(InvalidOTPException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("User is allready present please enter unique username").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingOTPExpiryException(OTPExpiryException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Invalid OTP.., OTP Expired...!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingRegistrationSessionExpiryException(RegistrationExpiryException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Registration Time is Expired.., Please Try Again Later..!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingInvalidEmailException(InvalidEmailException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Invalid Email.., Please Enter The Valid Email..!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingConnectException(ConnectException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Please Check The NetworkConnection...!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingMailConnectException(MailConnectException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.BAD_REQUEST.value())
  				.setMessage("Please Check The Networ Connection..!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingInvalidCredentialsException(InvalidCredentialsException e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.UNAUTHORIZED.value())
  				.setMessage("Authentication Failed....!").setRootcause(e.getMessage()));	
  	}
    @ExceptionHandler
  	public ResponseEntity<ErrorStructure<String>> handlingUserNotExistException(UserIsNotLogin e) {
  		return ResponseEntity.badRequest().body(errorStructure.setStatus(HttpStatus.NOT_FOUND.value())
  				.setMessage("User Not Logged in....!").setRootcause(e.getMessage()));	
  	}
    

}
