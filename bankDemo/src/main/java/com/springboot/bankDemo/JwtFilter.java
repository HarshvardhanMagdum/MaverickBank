package com.springboot.bankDemo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.bankDemo.service.CustomUserDetailsService;
import com.springboot.bankDemo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String username = null;
		     String jwt = null;
		     
			/* Check if Authorization header is present and fetch the token */
			final String authorizationHeader = request.getHeader("Authorization");
			
			/* Fetch the token from "Bearer <token>" */
			/* Extract the username/email from this token */
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwt = authorizationHeader.substring(7);
	            username = jwtUtil.extractUsername(jwt);
	        }
			
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				/* Verify in DB if this username exists */
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				
				/* Verify the Token */
				boolean isValid = jwtUtil.verifyToken(jwt, username); 
				if(isValid) {
					/* Authnticate: Set up Authentication - Log this user IN, allow the API access */
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
		                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		                usernamePasswordAuthenticationToken
		                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		}
		catch(Exception e) {
			 System.out.println(e);
		}
			
	}

}
