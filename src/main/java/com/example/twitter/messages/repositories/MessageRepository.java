package com.example.twitter.messages.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.twitter.messages.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
