package com.roy.blog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.roy.blog.models.Category;
import com.roy.blog.models.Post;
import com.roy.blog.models.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	Page<Post> findByuser(User user, Pageable pageable);
	
	Page<Post> findByCategory(Category category, Pageable pageable);

	List<Post> findByTitleContaining(String title);

}
