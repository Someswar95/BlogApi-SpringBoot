package com.roy.blog.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.roy.blog.playloads.PostByCategoryIdResponse;
import com.roy.blog.playloads.PostByUserIdResponse;
import com.roy.blog.playloads.PostDto;
import com.roy.blog.playloads.PostResponse;

public interface PostService {

	// create
	PostDto createPost(PostDto postDto, MultipartFile postFile, Integer userId, Integer categoryId);

	// update
	PostDto updatePost(PostDto postDto, MultipartFile postFile, Integer postId);

	// delete
	void deletePost(Integer postId);

	// get all posts
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	// get single post
	PostDto getPostById(Integer postId);

	// get all post by category
	PostByCategoryIdResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	// get all post by user
	PostByUserIdResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	// search posts
	List<PostDto> searchPosts(String keyword);

}
