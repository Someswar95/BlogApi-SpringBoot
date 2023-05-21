package com.roy.blog.playloads;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentDto {

	private int CommentId;

	@NotBlank(message = "Content cannot be null !!")
	private String content;

	private String commentDate;

}
