package com.example.jwt.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
