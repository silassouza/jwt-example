package com.example.jwt.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwt.users.entities.User;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");
		if (authorization != null) {
			String token = jwtUtil.extractToken(authorization);
			if (jwtUtil.isValid(token)) {

				User user = new User(
					jwtUtil.extractClaim(token, "userId", Long.class),
					jwtUtil.extractClaim(token, "username", String.class)
				);

				UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(user, null, null);
				WebAuthenticationDetailsSource wads = new WebAuthenticationDetailsSource();
				upa.setDetails(wads.buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upa);
			}
		}

		filterChain.doFilter(request, response);
	}

}
