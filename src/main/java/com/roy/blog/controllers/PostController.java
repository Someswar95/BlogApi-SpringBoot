package com.roy.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.blog.config.AppConstants;
import com.roy.blog.playloads.ApiResponse;
import com.roy.blog.playloads.PostByCategoryIdResponse;
import com.roy.blog.playloads.PostByUserIdResponse;
import com.roy.blog.playloads.PostDto;
import com.roy.blog.playloads.PostResponse;
import com.roy.blog.services.FileService;
import com.roy.blog.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ObjectMapper mapper;

	@Value("${project.image}")
	private String path;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	// create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestParam("postData") String postData,
			@RequestParam("postFile") MultipartFile postFile, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {

		PostDto postDto = null;
		// Converting String to JSON
		try {
			postDto = this.mapper.readValue(postData, PostDto.class);
		} catch (JsonProcessingException e) {
			logger.info("Invalid Request");
		}
		PostDto createdPost = this.postService.createPost(postDto, postFile, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> updatedPost(@RequestParam("postData") String postData,
			@RequestParam("postFile") MultipartFile postFile, @PathVariable Integer postId) {

		PostDto postDto = null;
		// Converting String to JSON
		try {
			postDto = this.mapper.readValue(postData, PostDto.class);
		} catch (JsonProcessingException e) {
			logger.info("Invalid Request");
		}
		PostDto updatePost = this.postService.updatePost(postDto, postFile, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostByUserIdResponse> getPostByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostByUserIdResponse postByUserIdResponse = this.postService.getPostsByUser(userId, pageNumber, pageSize,
				sortBy, sortDir);
		return ResponseEntity.ok(postByUserIdResponse);
	}

	// get by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostByCategoryIdResponse> getPostByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostByCategoryIdResponse postByCategoryIdResponse = this.postService.getPostsByCategory(categoryId, pageNumber,
				pageSize, sortBy, sortDir);
		return ResponseEntity.ok(postByCategoryIdResponse);
	}

	// get all post
	@GetMapping("/post/")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(postResponse);
	}

	// get single post
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDtos = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDtos, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
	}

	// search
	@GetMapping("/post/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keyword") String keyword) {
		List<PostDto> results = this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(results, HttpStatus.OK);
	}

	// method to serve files
	@GetMapping(value = "/post/image/{imageName}")
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
