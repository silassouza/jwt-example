package com.example.twitter.messages.resources;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.twitter.auth.JwtUtil;
import com.example.twitter.messages.entities.Message;
import com.example.twitter.messages.repositories.MessageRepository;

@RestController
@RequestMapping("messages")
public class MessageResource {

	@Value("${jwt.secret}")
	private String secret;

	private MessageRepository messageRepository;

	public MessageResource(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@GetMapping
	public Collection<Message> messages(@RequestHeader(name = "token") String token) {

		Long userId = JwtUtil.validate(secret, token);

		return messageRepository.findAll(Example.of(new Message(userId)));
	}

	@PostMapping
	public void insert(@RequestHeader(name = "token") String token, @RequestParam(name = "message") String message) {

		Long userId = JwtUtil.validate(secret, token);

		Message msg = new Message();
		msg.setUserId(userId);
		msg.setBody(message);
		msg.setDate(LocalDate.now());

		messageRepository.save(msg);
	}

	@DeleteMapping
	public void delete(@RequestHeader(name = "token") String token, @RequestParam(name = "messageId") Long messageId) {

		Long userId = JwtUtil.validate(secret, token);

		Optional<Message> message = messageRepository.findById(messageId);

		if (!message.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (message.get().getUserId() != userId) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		messageRepository.delete(message.get());
	}

	@PutMapping
	public void update(@RequestHeader(name = "token") String token, @RequestBody Message message) {

		Long userId = JwtUtil.validate(secret, token);

		if (message == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (message.getUserId() != userId) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
		if (!messageRepository.existsById(message.getMessageId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		messageRepository.save(message);
	}

}
