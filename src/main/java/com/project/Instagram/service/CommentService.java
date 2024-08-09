package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.Instagram.model.Comment;
import com.project.Instagram.model.Post;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.CommentRepository;
import com.project.Instagram.repository.PostRepository;
import com.project.Instagram.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Override
	public Comment createComment(Long userId, Long postId, String text, Boolean isRead) {
		//provera
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
		Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setPost(post);
		comment.setText(text);
		comment.setCreatedAt(LocalDateTime.now());
		comment.setIsRead(isRead);
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(Long commentId) {
		Optional<Comment> comment = commentRepository.findById(commentId);
		if(comment.isPresent()) {
			commentRepository.deleteById(commentId);
		}
	}

	@Override
	public Optional<Comment> getCommentById(Long commentId) {
		return commentRepository.findById(commentId);
	}

	@Override
	public List<Comment> getAllComments() {
		return commentRepository.findAll();
	}

	@Override
	public List<Comment> getCommentsByUser(Long userId) {
		return commentRepository.findCommentsByUser(userId);
	}

	@Override
	public List<Comment> getCommentsByPost(Long postId) {
		return commentRepository.findCommentsByPost(postId);
	}

	@Override
	public List<Comment> getCommentByUserAndPost(Long userId, Long postId) {
		return commentRepository.findCommentsByUserAndPost(userId, postId);
	}

	@Override
	public int countCommentsForPost(Long postId) {
		return commentRepository.countCommentsForPost(postId);
	}

	@Override
	public int countCommentByUser(Long userId) {
		return commentRepository.countCommenstByUser(userId);
	}

	@Override
	public List<Comment> searchCommentsByText(String text) {
		return commentRepository.findCommentsByText(text);
	}

	@Override
	public List<Comment> getCommentsAfterDate(LocalDateTime date) {
		return commentRepository.findCommentsAfterDate(date);
	}

	@Override
	public List<Comment> getCommentsContainingText(String text) {
		return commentRepository.findCommentsContainingText(text);
	}

	@Override
	public int countUnreadCommentsByUser(Long userId) {
		return commentRepository.countUnreadCommentsByUser(userId);
	}
}
