package com.example.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.auth.JwtUtil;

@Configuration
public class AppConfigurer {

	@Value("${jwt.secret}")
	private String secret;
	
	@Bean
	public JwtUtil jwtUtil() {
		return new JwtUtil(Algorithm.HMAC256(secret));
	}
}
