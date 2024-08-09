package com.project.Instagram.controller;

import java.sql.Blob;
import java.sql.SQLException;
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


import com.project.Instagram.exception.ErrorCreatingLikeException;
import com.project.Instagram.exception.PhotoRetrievalException;
import com.project.Instagram.model.LikeClass;
import com.project.Instagram.model.Post;
import com.project.Instagram.model.User;
import com.project.Instagram.request.LikeRequest;
import com.project.Instagram.response.LikeResponse;
import com.project.Instagram.response.PostResponse;
import com.project.Instagram.response.UserResponse;
import com.project.Instagram.service.ILikeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
	
	private final ILikeService likeService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add/new-like")
	public ResponseEntity<Object> createLike(@Valid @RequestBody LikeRequest likeRequest){
		try {
			LikeClass like = likeService.createLike(likeRequest.getUserId(),likeRequest.getPostId());
			PostResponse postResponse = new PostResponse(
					like.getPost().getId(),
					new UserResponse(like.getPost().getUser().getId(),like.getPost().getUser().getUsername(),
							like.getPost().getUser().getEmail(),like.getPost().getUser().getBio()),
					like.getPost().getCaption(),
					like.getPost().getImageUrl(),
					like.getPost().getVideoUrl(),
					like.getPost().getLocation(),
					like.getPost().getCreatedAt());
			LikeResponse likeResponse = new LikeResponse(like.getId(),like.getUser().getId(),like.getUser().getUsername(),postResponse,like.getCreatedAt());
			return ResponseEntity.ok(likeResponse);
		}
		catch(ErrorCreatingLikeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/like/{likeId}")
	public ResponseEntity<Void> deleteLike(@PathVariable Long likeId){
		likeService.deleteLike(likeId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/like/{likeId}")
	public ResponseEntity<Object> getLikeById(@PathVariable Long likeId){
		try {
			LikeClass like = likeService.getLikeById(likeId).orElseThrow(
					() -> new IllegalArgumentException("Like not found"));
			if(like != null) {
				LikeResponse response = getLikeResponse(like);
				return ResponseEntity.ok(response);
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
		catch(ErrorCreatingLikeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping("/all-likes")
	public ResponseEntity<List<LikeResponse>> getAllLikes(){
		List<LikeClass> likes = likeService.getAllLikes();
		List<LikeResponse> responses = likes.stream().map(this::getLikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<LikeResponse>> getLikesByUser(@PathVariable Long userId){
		List<LikeClass> likes = likeService.getLikesByUser(userId);
		List<LikeResponse> responses = likes.stream().map(this::getLikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<List<LikeResponse>> findLikesByPost(@PathVariable Long postId){
		List<LikeClass> likes = likeService.findLikesByPost(postId);
		List<LikeResponse> responses = likes.stream().map(this::getLikeResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/count-likes-by-post/{postId}")
	public ResponseEntity<Integer> countLikesByPost(@PathVariable Long postId){
		int countLikeByPost = likeService.countLikesByPost(postId);
		return ResponseEntity.ok(countLikeByPost);
	}
	
	
	@GetMapping("/count-likes-by-user/{userId}")
	public ResponseEntity<Integer> countLikesByUser(@PathVariable Long userId){
		int countLikesByUser = likeService.countLikesByUser(userId);
		return ResponseEntity.ok(countLikesByUser);
	}
	
	@GetMapping("/count-all-likes")
	public ResponseEntity<Integer> countAllLikes(){
		int allLikes = likeService.countAllLikes();
		return ResponseEntity.ok(allLikes);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/toggle")
	public ResponseEntity<Object> toggleLike(@RequestParam Long postId,@RequestParam Long userId){
		try {
			LikeClass like = likeService.toggleLike(postId, userId);
			if(like == null) {
				return ResponseEntity.ok("Like removed");
			}
			else {
				LikeResponse response = new LikeResponse(
						like.getId(),
		                like.getUser().getId(),
		                like.getUser().getUsername(),
		                new PostResponse(
		                    like.getPost().getId(),
		                    new UserResponse(
		                        like.getPost().getUser().getId(),
		                        like.getPost().getUser().getUsername(),
		                        like.getPost().getUser().getEmail(),
		                        like.getPost().getUser().getBio()
		                    ),
		                    like.getPost().getCaption(),
		                    like.getPost().getImageUrl(),
		                    like.getPost().getVideoUrl(),
		                    like.getPost().getLocation(),
		                    like.getPost().getCreatedAt(),
		                    like.getPost().getUpdatedAt()
		                ),
		                like.getCreatedAt());
				return ResponseEntity.ok(response);
			}
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/has-liked")
	public ResponseEntity<Boolean> userHasLikedPost(@RequestParam Long postId,@RequestParam Long userId){
		try {
			boolean hasLiked = likeService.userHasLikedPost(postId, userId);
			return ResponseEntity.ok(hasLiked);
		}
		catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/by-post-and-user")
	public ResponseEntity<Object> getLikesByPostAndUser(@RequestParam Long postId, @RequestParam Long userId){
		try {
			List<LikeClass> likes = likeService.getLikesByPostAndUser(postId, userId);
			List<LikeResponse> response = likes.stream().map(
					like -> new LikeResponse(
							like.getId(),
							like.getUser().getId(),
							like.getUser().getUsername(),
							new PostResponse(
									like.getPost().getId(),
									new UserResponse(like.getPost().getUser().getId(),
											like.getPost().getUser().getUsername(),
											like.getPost().getUser().getEmail(),
											like.getPost().getUser().getBio()),
									like.getPost().getCaption(),
									like.getPost().getImageUrl(),
									like.getPost().getVideoUrl(),
									like.getPost().getLocation(),
									like.getPost().getCreatedAt()),
							like.getCreatedAt())).collect(Collectors.toList());
			return ResponseEntity.ok(response);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	private LikeResponse getLikeResponse(LikeClass like) {
		User user = like.getUser();
		Post post = like.getPost();
		byte[] photoBytes = null;
        Blob photoBlob = user.getProfilePicture();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
		PostResponse postResponse = new PostResponse(post.getId(),
				new UserResponse(user.getId(), user.getUsername(),user.getEmail(),photoBytes, user.getBio()),
				post.getCaption(),
				post.getImageUrl(),
				post.getVideoUrl(),
				post.getLocation(),
				post.getCreatedAt());
		return new LikeResponse(like.getId(),like.getUser().getId(),like.getUser().getUsername(), postResponse);
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
