package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.LikeClass;
import com.project.Instagram.model.Post;

public interface ILikeService {

	LikeClass createLike(Long userId, String username, Post post, LocalDateTime createdAt);
	
	LikeClass createLike(LikeClass like);
	
	LikeClass createLike(Long userId, Long postId);
	
	void deleteLike(Long likeId);
	
	Optional<LikeClass> getLikeById(Long likeId);
	
	List<LikeClass> getAllLikes();
	
	List<LikeClass> getLikesByUser(Long userId);
	
	List<LikeClass> findLikesByPost(Long postId);
	
	int countLikesByPost(Long postId);
	
	int countLikesByUser(Long userId);
	
	int countAllLikes();
	
	LikeClass toggleLike(Long postId, Long userId);
	
	boolean userHasLikedPost(Long postId, Long userId);
	
	List<LikeClass> getLikesByPostAndUser(Long postId, Long userId);
}
