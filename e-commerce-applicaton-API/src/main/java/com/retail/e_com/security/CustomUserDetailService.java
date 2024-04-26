package com.retail.e_com.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.retail.e_com.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	
	private UserRepository userRepository;
	
	public CustomUserDetailService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByUserName(username).map(CustomUserDetail::new)
				.orElseThrow();
	}

}
