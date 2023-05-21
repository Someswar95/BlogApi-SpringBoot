package com.roy.blog.services.impl;

import java.text.DateFormat;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roy.blog.exception.ResourceNotFoundException;
import com.roy.blog.models.Comment;
import com.roy.blog.models.Post;
import com.roy.blog.models.User;
import com.roy.blog.playloads.CommentDto;
import com.roy.blog.repositories.CommentRepo;
import com.roy.blog.repositories.PostRepo;
import com.roy.blog.repositories.UserRepo;
import com.roy.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer userId, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post_id", postId));

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "user_id", userId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		Date currentDate = new Date();
		String formatDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(currentDate);
		comment.setCommentDate(formatDate);
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment com = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment_id", commentId));
		this.commentRepo.delete(com);
	}

}
