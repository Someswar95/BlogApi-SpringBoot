package com.roy.blog.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.roy.blog.config.AppConstants;
import com.roy.blog.exception.ResourceNotFoundException;
import com.roy.blog.models.Role;
import com.roy.blog.models.User;
import com.roy.blog.playloads.UserDto;
import com.roy.blog.repositories.RoleRepo;
import com.roy.blog.repositories.UserRepo;
import com.roy.blog.services.FileService;
import com.roy.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@Override
	public UserDto registerNewUser(UserDto userDto, MultipartFile profileImage) {
		User user = this.modelMapper.map(userDto, User.class);

		// encoded the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, profileImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.setProfileImage(fileName);

		// roles
		Role role = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
		user.getRoles().add(role);

		User newUser = this.userRepo.save(user);

		return this.modelMapper.map(newUser, UserDto.class);
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		this.userRepo.delete(user);

	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId, MultipartFile profileImage) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		user.setName(userDto.getPassword());
		user.setEmail(userDto.getEmail());

		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, profileImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.setProfileImage(fileName);

		User updatedUser = this.userRepo.save(user);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map((user) -> this.modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return userDtos;
	}

}
