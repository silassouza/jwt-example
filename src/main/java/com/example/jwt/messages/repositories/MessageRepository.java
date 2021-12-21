package com.example.jwt.messages.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt.messages.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
