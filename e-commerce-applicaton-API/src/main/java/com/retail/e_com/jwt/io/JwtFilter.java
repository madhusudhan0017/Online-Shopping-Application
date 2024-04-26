package com.retail.e_com.jwt.io;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.retail.e_com.Exception.InvalidCredentialsException;
import com.retail.e_com.jwt.repository.AccessTokenRepo;
import com.retail.e_com.jwt.repository.RefreshTokenRepo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	
	private AccessTokenRepo accessrepo;
	private RefreshTokenRepo refreshrepo;
	private JWTService jwtService;
	
	
	public JwtFilter(AccessTokenRepo accessrepo, RefreshTokenRepo refreshrepo, JWTService jwtService) {
		this.accessrepo = accessrepo;
		this.refreshrepo = refreshrepo;
		this.jwtService = jwtService;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String at = null;
		String rt = null;

		if(request.getCookies() != null) {
		for(Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals("at")) at = cookie.getValue();
			if(cookie.getName().equals("rt")) rt = cookie.getValue();		

		}
		}
		if(at!=null && rt!=null) {
			if(accessrepo.existsByTokenAndIsBlocked(at,true) &&
					refreshrepo.existsByTokenAndIsBlocked(rt,true)) throw new InvalidCredentialsException("Invalid Credential");
		
		String role = jwtService.getUserRole(at);
			String userName = jwtService.getuserName(at);
			
		
		
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null && role != null) {
			UsernamePasswordAuthenticationToken token =
					new UsernamePasswordAuthenticationToken(userName,null, 
							Collections.singleton(new SimpleGrantedAuthority(role)));
			token.setDetails(new WebAuthenticationDetails(request));
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		}
		try {
			
		
		filterChain.doFilter(request, response);
	}
		catch(ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWT token expired");
		}
		catch(JwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"jwt expired");
		}
	}
}
