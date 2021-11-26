package com.example.twitter.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.twitter.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
