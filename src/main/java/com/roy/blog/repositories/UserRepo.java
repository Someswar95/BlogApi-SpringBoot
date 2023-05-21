package com.roy.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roy.blog.models.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	public User findByEmail(String username);

}
