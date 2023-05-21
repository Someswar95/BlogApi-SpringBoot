package com.roy.blog.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.roy.blog.playloads.UserDto;

public interface UserService {

	// register new user
	UserDto registerNewUser(UserDto userDto, MultipartFile profileImage);

	// delete
	void deleteUser(Integer userId);

	// update
	UserDto updateUser(UserDto userDto, Integer userId, MultipartFile profileImage);

	// get
	UserDto getUserById(Integer userId);

	// get all
	List<UserDto> getAllUsers();

}
