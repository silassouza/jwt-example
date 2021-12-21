package com.example.jwt.messages.resources;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.jwt.messages.entities.Message;
import com.example.jwt.messages.repositories.MessageRepository;
import com.example.jwt.users.entities.User;

@RestController
@RequestMapping("messages")
public class MessageResource {

	private MessageRepository messageRepository;

	public MessageResource(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@GetMapping
	public Collection<Message> messages(Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		return messageRepository.findAll(Example.of(new Message(user.getUserId())));
	}

	@PostMapping
	public void insert(Authentication authentication, @RequestParam(name = "message") String message) {

		User user = (User) authentication.getPrincipal();

		Message msg = new Message();
		msg.setUserId(user.getUserId());
		msg.setBody(message);
		msg.setDate(LocalDate.now());

		messageRepository.save(msg);
	}

	@DeleteMapping
	public void delete(Authentication authentication, @RequestParam(name = "messageId") Long messageId) {

		User user = (User) authentication.getPrincipal();

		Optional<Message> message = messageRepository.findById(messageId);

		if (!message.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (message.get().getUserId() != user.getUserId()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		messageRepository.delete(message.get());
	}

	@PutMapping
	public void update(Authentication authentication, @RequestBody Message message) {

		User user = (User) authentication.getPrincipal();

		if (message == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		if (message.getUserId() != user.getUserId()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		if (!messageRepository.existsById(message.getMessageId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		messageRepository.save(message);
	}

}
