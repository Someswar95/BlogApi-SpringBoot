package com.roy.blog.config;

public class AppConstants {

	public static final String secret = "jwtTokenKey";
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	public static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;
	public static final Integer ROLE_NORMAL = 502;
	public static final Integer ROLE_ADMIN = 501;
	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "10";
	public static final String SORT_BY = "postId";
	public static final String SORT_DIR = "asc";

}
