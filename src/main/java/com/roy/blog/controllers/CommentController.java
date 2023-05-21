package com.roy.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roy.blog.playloads.ApiResponse;
import com.roy.blog.playloads.CommentDto;
import com.roy.blog.services.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// create comment
	@PostMapping("/user/{userId}/post/{postId}/comments")
	@PreAuthorize(value = "ROLE_NORMAL")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
			@PathVariable Integer userId,
			@PathVariable Integer postId) {
		CommentDto savedComment = this.commentService.createComment(commentDto, userId, postId);
		return new ResponseEntity<CommentDto>(savedComment, HttpStatus.CREATED);
	}

	// delete comment
	@DeleteMapping("/post/comments/{commentId}")
	@PreAuthorize(value = "ROLE_NORMAL")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully !!", true), HttpStatus.OK);
	}

}
