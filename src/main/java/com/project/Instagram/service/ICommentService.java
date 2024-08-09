package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.Comment;

public interface ICommentService {

	Comment createComment(Long userId, Long postId, String text, Boolean isRead);
	
	void deleteComment(Long commentId);
	
	Optional<Comment> getCommentById(Long commentId);
	
	List<Comment> getAllComments();
	
	List<Comment> getCommentsByUser(Long userId);
	
	List<Comment> getCommentsByPost(Long postId);
	
	int countCommentByUser(Long userId);
	
	int countUnreadCommentsByUser(Long userId);
	
	List<Comment> getCommentByUserAndPost(Long userId, Long postId);
	
	int countCommentsForPost(Long postId);
	
	List<Comment> searchCommentsByText(String text);
	
	List<Comment> getCommentsAfterDate(LocalDateTime date);
	
	List<Comment> getCommentsContainingText(String text);
}
