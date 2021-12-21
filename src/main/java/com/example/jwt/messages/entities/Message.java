package com.example.jwt.messages.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "message")
public class Message {
	
	public Message() { }
	
	public Message(Long userId) { 
		this.userId = userId;
	}
	
	@Id
	@SequenceGenerator(name = "message_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
	@Column(name = "messageid")
	private Long messageId;
	
	@NotNull
	@Column(name = "userid")
	private Long userId;
	
	@NotNull
	@Column(name = "body")
	private String body;
	
	@NotNull
	@Column(name = "datetime")
	private LocalDate date;

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
