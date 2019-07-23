package com.seol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seol.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
