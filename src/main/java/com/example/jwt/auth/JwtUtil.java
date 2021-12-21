package com.example.jwt.auth;

import java.util.IllegalFormatWidthException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.jwt.users.entities.User;

public class JwtUtil {

	private Algorithm algorithm;

	private JWTVerifier verifier;

	public JwtUtil(Algorithm algorithm) {
		this.algorithm = algorithm;
		this.verifier = JWT.require(algorithm).build();
	}

	public String generate(User user) {
		String token = JWT.create()
				.withClaim("userId", user.getUserId())
				.withClaim("username", user.getUsername())
				.sign(algorithm);
		return token;
	}

	public boolean isValid(String token) {
		try {
			verifier.verify(token);
		} catch (JWTVerificationException e) {
			return false;
		}
		return true;
	}

	public String extractToken(String authorizarion) {
		if (authorizarion == null || authorizarion.length() < 7 || !authorizarion.startsWith("Bearer ")) {
			throw new IllegalFormatWidthException(7);
		}
		return authorizarion.substring(7);
	}

	public <T> T extractClaim(String token, String claim, Class<T> returnType) {
		DecodedJWT jwt = verifier.verify(token);
		T userData = jwt.getClaims().get(claim).as(returnType);
		return userData;
	}

}
