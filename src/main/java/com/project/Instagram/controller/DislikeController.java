package com.project.Instagram.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.project.Instagram.exception.ErrorCreatingDislikeException;
import com.project.Instagram.model.Dislike;
import com.project.Instagram.model.User;
import com.project.Instagram.request.DislikeRequest;
import com.project.Instagram.response.DislikeResponse;
import com.project.Instagram.service.IDislikeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequestMapping("/dislikes")
@RequiredArgsConstructor
public class DislikeController {

	private final IDislikeService dislikeService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add/new-dislike")
	public ResponseEntity<Object> createDislike(@Valid @RequestBody DislikeRequest request){
		try {
			Dislike dislike = dislikeService.createDislike(request.getUserId(), request.getPostId());
			DislikeResponse response = new DislikeResponse(dislike.getId(),
					dislike.getUser().getId(),
					dislike.getUser().getUsername(),
					dislike.getPost().getId(),
					dislike.getCreateAt());
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingDislikeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/dislike/{dislikeId}")
	public ResponseEntity<Void> deleteDislike(@PathVariable Long dislikeId){
		dislikeService.deleteDislike(dislikeId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{dislikeId}")
	public ResponseEntity<Object> getDislikeById(@PathVariable Long dislikeId){
		try {
			Dislike dislike = dislikeService.getDislikeById(dislikeId).orElseThrow(
					() -> new IllegalArgumentException("Dislike not found " + dislikeId));
			return ResponseEntity.ok(dislike);
		}
		catch(ErrorCreatingDislikeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping("/all-dislikes")
	public ResponseEntity<List<DislikeResponse>> getAllDislike(){
		List<Dislike> dislikes = dislikeService.getAllDislike();
		List<DislikeResponse> responses = dislikes.stream().map(this::getDislikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<List<DislikeResponse>> getDislikesByPost(@PathVariable Long postId){
		List<Dislike> dislikes = dislikeService.getDislikesByPost(postId);
		List<DislikeResponse> responses = dislikes.stream().map(this::getDislikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<DislikeResponse>> getDislikesByUser(@PathVariable Long userId){
		List<Dislike> dislikes = dislikeService.getDislikesByUser(userId);
		List<DislikeResponse> responses = dislikes.stream().map(this::getDislikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/by-post-and-user")
	public ResponseEntity<Object> getDislikesByPostAndUser(@RequestParam Long postId,@RequestParam Long userId){
		try {
			List<Dislike> dislikes = dislikeService.getDislikesByPostAndUser(postId, userId);
			List<DislikeResponse> responses = dislikes.stream().map(dislike -> new DislikeResponse(
					dislike.getId(),
					dislike.getUser().getId(),
					dislike.getUser().getUsername(),
					dislike.getPost().getId(),
					dislike.getCreateAt())).collect(Collectors.toList());
			return ResponseEntity.ok(responses);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping("/count-dislikes-by-post/{postId}")
	public ResponseEntity<Integer> countDislikesByPost(@PathVariable Long postId){
		int countDislikes = dislikeService.countDislikesByPost(postId);
		return ResponseEntity.ok(countDislikes);
	}
	
	@GetMapping("/count-dislikes-by-user/{userId}")
	public ResponseEntity<Integer> countDislikesByUser(@PathVariable Long userId){
		int countDislikes = dislikeService.countDislikesByUser(userId);
		return ResponseEntity.ok(countDislikes);
	}
	
	@GetMapping("/count-all-dislikes")
	public ResponseEntity<Integer> countAllDislikes(){
		int countAll = dislikeService.countAllDislikes();
		return ResponseEntity.ok(countAll);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/toggle")
	public ResponseEntity<Object> toggleDislike(@RequestParam Long postId,@RequestParam Long userId){
		try {
			Dislike dislike = dislikeService.toggleDislike(postId, userId);
			if(dislike == null) {
				return ResponseEntity.ok("Dislike removed");
			}
			else {
				DislikeResponse response = new DislikeResponse(dislike.getId(),
						dislike.getUser().getId(),
						dislike.getUser().getUsername(),
						dislike.getPost().getId(),
						dislike.getCreateAt());
				return ResponseEntity.ok(response);
			}
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/has-disliked")
	public ResponseEntity<Boolean> userHasDislikedPost(@RequestParam Long postId,@RequestParam Long userId){
		try {
			boolean hasDisliked = dislikeService.userHasDislikedPost(postId, userId);
			return ResponseEntity.ok(hasDisliked);
		}
		catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
	}
	
	private DislikeResponse getDislikeResponse(Dislike dislike) {
		User user = dislike.getUser();
		return new DislikeResponse(dislike.getId(),
				user.getId(),
				user.getUsername(),
				dislike.getPost().getId(),
				dislike.getCreateAt());
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
