package com.roy.blog.playloads;

import lombok.Data;

@Data
public class JwtAuthResponse {

	private String token;

	private UserDto user;

}
