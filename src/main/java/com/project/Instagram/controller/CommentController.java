package com.project.Instagram.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Instagram.exception.ErrorCreatingCommentException;
import com.project.Instagram.model.Comment;
import com.project.Instagram.response.CommentResponse;
import com.project.Instagram.service.ICommentService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

	
	private ICommentService commentService;
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/create/new-comment")
	public ResponseEntity<Object> createComment(
			@RequestParam("userId") Long userId,
            @RequestParam("postId") Long postId,
            @RequestParam("text") String text,
            @RequestParam("isRead") Boolean isRead){
		try {
			Comment comment = commentService.createComment(userId, postId, text, isRead);
			CommentResponse response = new CommentResponse(
					comment.getId(),
					comment.getUser().getId(),
					comment.getPost().getId(),
					comment.getText(),
					comment.getCreatedAt());
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingCommentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@DeleteMapping("/delete/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
		commentService.deleteComment(commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{commentId}")
	public ResponseEntity<Object> getCommentById(@PathVariable Long commentId){
		try {
			Comment comment = commentService.getCommentById(commentId).orElseThrow(
					() -> new IllegalArgumentException("Comment not found " + commentId));
			CommentResponse response = getCommentResponse(comment);
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingCommentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping("/all-comments")
	public ResponseEntity<List<CommentResponse>> getAllComments(){
		List<Comment> comments = commentService.getAllComments();
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/comment/{userId}")
	public ResponseEntity<List<CommentResponse>> getCommentsByUser(@PathVariable Long userId){
		List<Comment> comments = commentService.getCommentsByUser(userId);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/comment/{postId}")
	public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable Long postId){
		List<Comment> comments = commentService.getCommentsByPost(postId);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/count-comments/{userId}")
	public ResponseEntity<Integer> countCommentByUser( Long userId){
		int count = commentService.countCommentByUser(userId);
		return ResponseEntity.ok(count);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/count-unread/{userId}")
	public ResponseEntity<Integer> countUnreadCommentsByUser(Long userId){
		int count = commentService.countUnreadCommentsByUser(userId);
		return ResponseEntity.ok(count);
	}
	
	@GetMapping("/comments-by-user")
	public ResponseEntity<List<CommentResponse>> getCommentByUserAndPost(@RequestParam Long userId,@RequestParam Long postId){
		List<Comment> comments = commentService.getCommentByUserAndPost(userId, postId);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/count-coms-for-post")
	public ResponseEntity<Integer> countCommentsForPost(Long postId){
		int count = commentService.countCommentsForPost(postId);
		return ResponseEntity.ok(count);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/search-by-text")
	public ResponseEntity<List<CommentResponse>> searchCommentsByText(@RequestParam String text){
		List<Comment> comments = commentService.searchCommentsByText(text);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/after-date")
	public ResponseEntity<List<CommentResponse>> getCommentsAfterDate(LocalDateTime date){
		List<Comment> comments = commentService.getCommentsAfterDate(date);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/containing-text")
	public ResponseEntity<List<CommentResponse>> getCommentsContainingText(String text){
		List<Comment> comments = commentService.getCommentsContainingText(text);
		List<CommentResponse> responses = comments.stream().map(this::getCommentResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	private CommentResponse getCommentResponse(Comment comment) {
		return new CommentResponse(comment.getId(),
				comment.getUser().getId(),
				comment.getPost().getId(),
				comment.getText(),
				comment.getCreatedAt());
	}
	
	//ErrorResponse class for returning error messages
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class ErrorResponse {
		   private String message;
	}
}
