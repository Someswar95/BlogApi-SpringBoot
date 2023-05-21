package com.roy.blog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int CommentId;

	@Column(name = "comment_content", length = 1000000000, nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "comment_date", nullable = false)
	private String commentDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
