package com.example.twitter.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		if(authorization != null) {
			String token = JwtUtil.extractToken(authorization); 
			if (JwtUtil.isValid(secret, token)) {

				String username = JwtUtil.extractClaim(secret, token, "username", String.class);

				UserDetails user = userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(user, null, null);
				WebAuthenticationDetailsSource wads = new WebAuthenticationDetailsSource();
				upa.setDetails(wads.buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upa);
			}
		}
		filterChain.doFilter(request, response);
	}

}
