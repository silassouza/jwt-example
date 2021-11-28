package com.example.twitter.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

	public static String generate(String secret, Long userId) {
		Algorithm algorithm = Algorithm.HMAC256(secret);
		String token = JWT.create()
				.withClaim("userId", userId)
				.sign(algorithm);
		return token;
	}

	public static Long validate(String secret, String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			Long userId = jwt.getClaims().get("userId").asLong();
			return userId;
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
	}

}
