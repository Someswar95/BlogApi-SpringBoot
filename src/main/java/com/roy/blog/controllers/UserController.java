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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.blog.playloads.ApiResponse;
import com.roy.blog.playloads.UserDto;
import com.roy.blog.services.FileService;
import com.roy.blog.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ObjectMapper mapper;

	@Value("${project.image}")
	private String path;

	// DELETE -delete user
	@DeleteMapping("delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId) {
		this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestParam("user") String user,
			@RequestParam("profileImage") MultipartFile profileImage, @PathVariable("userId") Integer userId) {

		UserDto userDto = null;
		// Converting String to JSON
		try {
			userDto = this.mapper.readValue(user, UserDto.class);
		} catch (JsonProcessingException e) {
			logger.info("Invalid Request");
		}

		UserDto updatedUser = this.userService.updateUser(userDto, userId, profileImage);
		return ResponseEntity.ok(updatedUser);
	}

	// GET -user get
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	// GET -user get
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUsers(@PathVariable("userId") Integer userId) {
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

	// method to serve files
	@GetMapping(value = "/profile/{profileImage}")
	public void downloadImage(@PathVariable("profileImage") String profileImage, HttpServletResponse response)
			throws IOException {
		InputStream resource = this.fileService.getResource(path, profileImage);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
