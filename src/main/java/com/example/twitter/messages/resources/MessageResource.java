package com.example.twitter.messages.resources;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.twitter.messages.entities.Message;
import com.example.twitter.messages.repositories.MessageRepository;
import com.example.twitter.users.entities.User;
import com.example.twitter.users.repositories.UserRepository;

@RestController
@RequestMapping("messages")
public class MessageResource {

	private User loggedUser;

	private UserRepository userRepository;

	private MessageRepository messageRepository;

	public MessageResource(UserRepository userRepository, MessageRepository messageRepository) {
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
	}

	private void authenticate(String username, String password) {

		Optional<User> opUser = userRepository.findOne(Example.of(new User(username, password)));

		if (!opUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		this.loggedUser = opUser.get();
	}

	@GetMapping
	public Collection<Message> messages(@RequestHeader(name = "username") String username,
			@RequestHeader(name = "password") String password) {

		authenticate(username, password);

		return messageRepository.findAll();
	}

	@PostMapping
	public void insert(@RequestHeader(name = "username") String username,
			@RequestHeader(name = "password") String password, 
			@RequestParam(name = "message") String message) {

		authenticate(username, password);
		
		Message msg = new Message();
		msg.setUserId(loggedUser.getUserId());
		msg.setBody(message);
		msg.setDate(LocalDate.now());

		messageRepository.save(msg);
	}

	@DeleteMapping
	public void delete(@RequestHeader(name = "username") String username,
			@RequestHeader(name = "password") String password, @RequestParam(name = "messageId") Long messageId) {

		authenticate(username, password);

		messageRepository.deleteById(messageId);
	}

}
