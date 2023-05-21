package com.roy.blog.services.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.roy.blog.exception.ResourceNotFoundException;
import com.roy.blog.models.Category;
import com.roy.blog.models.Post;
import com.roy.blog.models.User;
import com.roy.blog.playloads.PostByCategoryIdResponse;
import com.roy.blog.playloads.PostByUserIdResponse;
import com.roy.blog.playloads.PostDto;
import com.roy.blog.playloads.PostResponse;
import com.roy.blog.repositories.CategoryRepo;
import com.roy.blog.repositories.PostRepo;
import com.roy.blog.repositories.UserRepo;
import com.roy.blog.services.FileService;
import com.roy.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@Override
	public PostDto createPost(PostDto postDto, MultipartFile postFile, Integer userId, Integer categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, postFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName(fileName);
		Date currentDate = new Date();
		String formatDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(currentDate);
		post.setAddedDate(formatDate);
		post.setUser(user);
		post.setCategory(category);
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, MultipartFile postFile, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, postFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(fileName);
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allpost = pagePost.getContent();
		List<PostDto> postDtos = allpost.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostByCategoryIdResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepo.findByCategory(cat, p);
		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostByCategoryIdResponse postByCategoryIdResponse = new PostByCategoryIdResponse();
		postByCategoryIdResponse.setContent(postDtos);
		postByCategoryIdResponse.setPageNumber(pagePost.getNumber());
		postByCategoryIdResponse.setPageSize(pagePost.getSize());
		postByCategoryIdResponse.setTotalElements(pagePost.getTotalElements());
		postByCategoryIdResponse.setTotalPages(pagePost.getTotalPages());
		postByCategoryIdResponse.setLastPage(pagePost.isLast());
		return postByCategoryIdResponse;
	}

	@Override
	public PostByUserIdResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		User users = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepo.findByuser(users, p);
		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostByUserIdResponse postByUserIdResponse = new PostByUserIdResponse();
		postByUserIdResponse.setContent(postDtos);
		postByUserIdResponse.setPageNumber(pagePost.getNumber());
		postByUserIdResponse.setPageSize(pagePost.getSize());
		postByUserIdResponse.setTotalElements(pagePost.getTotalElements());
		postByUserIdResponse.setTotalPages(pagePost.getTotalPages());
		postByUserIdResponse.setLastPage(pagePost.isLast());

		return postByUserIdResponse;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

}
