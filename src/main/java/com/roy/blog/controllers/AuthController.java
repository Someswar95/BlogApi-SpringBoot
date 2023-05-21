package com.roy.blog.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roy.blog.exception.ApiException;
import com.roy.blog.models.User;
import com.roy.blog.playloads.JwtAuthRequest;
import com.roy.blog.playloads.JwtAuthResponse;
import com.roy.blog.playloads.UserDto;
import com.roy.blog.security.JwtTokenHelper;
import com.roy.blog.services.UserService;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ObjectMapper mapper;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

		this.authenticate(request.getUsername(), request.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		String token = this.jwtTokenHelper.generateToken(userDetails);
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.modelMapper.map((User) userDetails, UserDto.class));
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {

			this.authenticationManager.authenticate(authenticationToken);

		} catch (BadCredentialsException e) {

			logger.info("Invalid Details !!");
			// throw new ApiException("Please try to login with correct credentials");
			throw new ApiException("Access Denined !!");
		}

	}

	// register new user
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestParam("user") String user,
			@RequestParam("profileImage") MultipartFile profileImage) {

		UserDto userDto = null;
		// Converting String to JSON
		try {
			userDto = this.mapper.readValue(user, UserDto.class);
		} catch (JsonProcessingException e) {
			logger.info("Invalid Request");
		}

		UserDto registeredUser = this.userService.registerNewUser(userDto, profileImage);
		return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);

	}

}
