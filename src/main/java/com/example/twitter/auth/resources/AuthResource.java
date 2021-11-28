package com.example.twitter.auth.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.twitter.auth.JwtUtil;
import com.example.twitter.users.entities.User;
import com.example.twitter.users.repositories.UserRepository;

@RestController
public class AuthResource {

	@Value("${jwt.secret}")
	private String secret;
	
	private UserRepository userRepository;

	public AuthResource(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public String authenticate(
			@RequestHeader(name = "username") String username,
			@RequestHeader(name = "password") String password) {
		
		Optional<User> opUser = userRepository.findOne(Example.of(new User(username, password)));

		if (!opUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		return JwtUtil.generate(secret, opUser.get().getUserId());
	}

}
