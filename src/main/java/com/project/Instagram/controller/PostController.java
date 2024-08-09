package com.project.Instagram.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Instagram.exception.ErrorCreatingPostException;
import com.project.Instagram.model.Post;
import com.project.Instagram.model.User;
import com.project.Instagram.request.CreatePostRequest;
import com.project.Instagram.response.PostResponse;
import com.project.Instagram.response.UserResponse;
import com.project.Instagram.service.IPostService;
import com.project.Instagram.service.IUserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@CrossOrigin("")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

	
	private final IPostService postService;
	private final IUserService userService;
	
	/*@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/add/new-post")
	public ResponseEntity<PostResponse> createPost(
			@RequestParam("caption") String caption,
			@RequestParam("imageUrl") String imageUrl,
			@RequestParam("videoUrl") String videoUrl,
			@RequestParam("location") String location,
			@RequestParam("createdAt") LocalDateTime createdAt) throws SQLException, IOException{
		Post savedPost = postService.createPost(caption, imageUrl, videoUrl, location, createdAt);
		PostResponse postResponse = getPostReponse(savedPost);
		return ResponseEntity.ok(postResponse);
	}*/
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/add/new-post")
	public ResponseEntity<Object> createPost(@Valid @RequestBody CreatePostRequest postRequest){
		try {
			Post post = postService.createPost(postRequest.getCaption(), postRequest.getImageUrl(),
					postRequest.getVideoUrl(), postRequest.getLocation(), postRequest.getCreatedAt());
			PostResponse postResponse = new PostResponse(post.getId(), new UserResponse(post.getUser().getId(),
					post.getUser().getUsername(),
					post.getUser().getEmail(),
					post.getUser().getBio()),post.getCaption(),post.getImageUrl(),
					post.getVideoUrl(),post.getLocation(),post.getCreatedAt());
			return ResponseEntity.ok(postResponse);
		}
		catch(ErrorCreatingPostException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PutMapping("/update/{postId}")
	public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
			@RequestParam(required = false) String caption,
			@RequestParam(required = false) String imageUrl,
			@RequestParam(required = false) String videoUrl,
			@RequestParam(required = false) String location){
		Post thePost = postService.updatePost(postId, caption, imageUrl, videoUrl, location);
		PostResponse postResponse = getPostResponse(thePost);
		return ResponseEntity.ok(postResponse);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@DeleteMapping("/delete/post/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable Long postId){
		postService.deletePost(postId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/all-posts")
	public ResponseEntity<List<PostResponse>> getAllPost(){
		List<Post> posts = postService.getAllPosts();
		List<PostResponse> postResponses = new ArrayList<>();
		for(Post p : posts) {
			PostResponse response = getPostResponse(p);
			postResponses.add(response);
		}
		return ResponseEntity.ok(postResponses);
	}
	
	// Dobavljanje jednog posta
    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable Long postId) {
    	try {
    		Post post = postService.getOne(postId);
    		if(post != null) {
    			PostResponse response = getPostResponse(post);
    			return ResponseEntity.ok(response);
    		}
    		else {
                return ResponseEntity.notFound().build();
            }
    	}
    	catch (ErrorCreatingPostException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<PostResponse>> getPostByUserId(@PathVariable Long userId){
		List<Post> posts = postService.getPostByUserId(userId);
		List<PostResponse> responses = posts.stream().map(
				this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/like/{likeId}")
	public ResponseEntity<List<PostResponse>> getPostByLikeId(@PathVariable Long likeId){
		List<Post> posts = postService.getPostByLikeId(likeId);
		List<PostResponse> responses = posts.stream().map(this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/comment/{commentId}")
	public ResponseEntity<List<PostResponse>> getPostByCommentId(@PathVariable Long commentId) {
		List<Post> posts = postService.getPostByCommentId(commentId);
		List<PostResponse> responses = posts.stream().map(this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/tag/{tagId}")
	public ResponseEntity<List<PostResponse>> getPostsByTagId(@PathVariable Long tagId){
		List<Post> posts = postService.getPostsByTagId(tagId);
		List<PostResponse> responses = posts.stream().map(this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/date-range")
	public ResponseEntity<List<PostResponse>> getPostsByDateRange(
	        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
	        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
		List<Post> posts = postService.getPostByDateRange(startDate, startDate);
		List<PostResponse> responses = posts.stream().map(this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/posts/sorted")
	public ResponseEntity<List<PostResponse>> getPostsSorted(
	        @RequestParam("sortBy") String sortBy,
	        @RequestParam("sortDirection") String sortDirection) {
		List<Post> posts = postService.getPostsSorted(sortBy, sortDirection);
		List<PostResponse> responses = posts.stream().map(this::getPostResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/post/{postId}/like-count")
	public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
		int likeCount = postService.getLikeCount(postId);
		return ResponseEntity.ok(likeCount);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/post/{postId}/comment-count")
	public ResponseEntity<Integer> getCommentCount(@PathVariable Long postId) {
		int commentCount = postService.getCommentCount(postId);
		return ResponseEntity.ok(commentCount);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/post/{postId}/add-tag")
	public ResponseEntity<PostResponse> addTagToPost(@PathVariable Long postId, @RequestParam("tagId") Long tagId){
		Post post = postService.addTagToPost(postId, tagId);
		PostResponse response = getPostResponse(post);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/post/{postId}/remove-tag")
	public ResponseEntity<PostResponse> removeTagFromPost(@PathVariable Long postId, @RequestParam("tagId") Long tagId) {
		Post post = postService.removeTagFromPost(postId, tagId);
		PostResponse response = getPostResponse(post);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/post/{postId}")
	public ResponseEntity<Object> getPostById(@PathVariable Long postId){
		try {
			Post post = postService.getOne(postId);
			PostResponse postResponse = getPostResponse(post);
			return ResponseEntity.ok(postResponse);
		}
		catch(EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	private PostResponse getPostResponse(Post post) {
		User user = userService.getUser(post.getUser().getEmail());
		UserResponse userResponse = new UserResponse(user.getId(),user.getUsername(),user.getEmail(),user.getBio());
		return new PostResponse(post.getId(),
				userResponse,
				post.getCaption(),
				post.getImageUrl(),
				post.getVideoUrl(),
				post.getLocation(),
				post.getCreatedAt());
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
