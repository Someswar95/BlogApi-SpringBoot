package com.roy.blog.playloads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

	private int categoryId;

	@NotBlank(message = "Title cannot be null !!")
	private String categoryTitle;

	@NotBlank(message = "Description cannot be null !!")
	@Size(min = 50, message = "Description should be atleast 50 characters")
	@Size(max = 500, message = "Description should be maximum 500 characters")
	private String categoryDescription;

}
