package com.roy.blog.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String fieldValue;

	public UserNotFoundException(String string) {
		super(String.format("Could not find any user with the email : %s", string));
		this.fieldValue = string;
	}

}
