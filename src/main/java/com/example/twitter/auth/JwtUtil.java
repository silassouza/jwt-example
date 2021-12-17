package com.example.twitter.auth;

import java.util.IllegalFormatException;
import java.util.IllegalFormatWidthException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.twitter.users.entities.User;

public class JwtUtil {

	public static String generate(String secret, User user) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		String token = JWT.create()
				.withClaim("userId", user.getUserId())
				.withClaim("username", user.getUsername())
				.sign(algorithm);
		return token;
	}

	public static boolean isValid(String secret, String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String extractToken(String authorizarion) {
		if (authorizarion == null 
				|| authorizarion.length() < 7 
				|| !authorizarion.startsWith("Bearer ")) {
			
			throw new IllegalFormatWidthException(7);		
		}
		return authorizarion.substring(7);
	}
	
	public static <T> T extractClaim(String secret, String token, String claim, Class<T> returnType) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(token);
		T userData = jwt.getClaims().get(claim).as(returnType);
		return userData;
	}

}
