package com.project.Instagram.service;

import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.Dislike;

public interface IDislikeService {

	Dislike createDislike(Long userId, Long postId);

	void deleteDislike(Long dislikeId);

	Optional<Dislike> getDislikeById(Long dislikeId);
	
	List<Dislike> getAllDislike();

	List<Dislike> getDislikesByPost(Long postId);

	List<Dislike> getDislikesByUser(Long userId);
	
	boolean userHasDislikedPost(Long postId, Long userId);
	
	int countDislikesByPost(Long postId);
	
	int countDislikesByUser(Long userId);

	int countAllDislikes();
	
	Dislike toggleDislike(Long postId, Long userId);
	
	List<Dislike> getDislikesByPostAndUser(Long postId, Long userId);
}
