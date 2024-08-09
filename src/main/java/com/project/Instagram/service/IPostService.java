package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;

import com.project.Instagram.model.Post;

public interface IPostService {

	
	Post createPost(String caption, String imageUrl, String videoUrl, String location, LocalDateTime createdAt);
	
	Post updatePost(Long postId, String caption, String imageUrl, String videoUrl, String location);
	
	void deletePost(Long postId);
	
	Post getOne(Long postId);
	
	List<Post> getAllPosts();
	
	List<Post> getPostByUserId(Long userId);
	
	List<Post> getPostByLikeId(Long likeId);
	
	List<Post> getPostByCommentId(Long commentId);
	
	List<Post> getPostsByTagId(Long tagId);
	
	List<Post> getPostByDateRange(LocalDateTime startDate, LocalDateTime endtDate);
	
	//sortiranje
	List<Post> getPostsSorted(String sortBy, String sortDirection);
	
	//broj lajkova za post
	int getLikeCount(Long postId);
	
	//broj komentara za post
	int getCommentCount(Long postId);
	
	Post addTagToPost(Long postId, Long tagId);

    Post removeTagFromPost(Long postId, Long tagId);
}
