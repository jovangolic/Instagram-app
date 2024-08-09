package com.project.Instagram.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {

	private Long id;
	
	private Long userId;
	
	private Long postId;
	
	private String text;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	public CommentResponse(Long id, String text, LocalDateTime createdAt) {
		this.id = id;
		this.text = text;
		this.createdAt = createdAt;
	}
	
	public CommentResponse(Long id, String text, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.text = text;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public CommentResponse(Long id, Long userId, Long postId, String text, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.postId = postId;
		this.text = text;
		this.createdAt = createdAt;
	}
}
