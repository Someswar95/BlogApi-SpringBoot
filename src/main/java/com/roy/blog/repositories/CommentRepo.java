package com.roy.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roy.blog.models.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
