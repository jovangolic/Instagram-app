package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.Instagram.model.Dislike;
import com.project.Instagram.model.Post;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.DislikeRepository;
import com.project.Instagram.repository.PostRepository;
import com.project.Instagram.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DislikeService implements IDislikeService {
	
	private final DislikeRepository dislikeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Override
	public Dislike createDislike(Long userId, Long postId) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	    Post post = postRepository.findById(postId)
	            .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
	    Dislike dislike = new Dislike();
	    dislike.setUser(user);
	    dislike.setPost(post);
		return dislikeRepository.save(dislike);
	}

	@Override
	public void deleteDislike(Long dislikeId) {
		Optional<Dislike> dislike = dislikeRepository.findById(dislikeId);
		if(dislike.isPresent()) {
			dislikeRepository.deleteById(dislikeId);
		}
	}

	@Override
	public Optional<Dislike> getDislikeById(Long dislikeId) {
		return dislikeRepository.findById(dislikeId);
	}

	@Override
	public List<Dislike> getDislikesByPost(Long postId) {
		return dislikeRepository.findDislikesByPost(postId);
	}

	@Override
	public List<Dislike> getDislikesByUser(Long userId) {
		return dislikeRepository.findDislikesByUser(userId);
	}

	@Override
	public boolean userHasDislikedPost(Long postId, Long userId) {
		return dislikeRepository.userHasDislikedPost(postId, userId);
	}

	@Override
	public int countDislikesByPost(Long postId) {
		return dislikeRepository.countDislikesByPost(postId);
	}

	@Override
	public int countDislikesByUser(Long userId) {
		return dislikeRepository.countDislikesByUser(userId);
	}

	@Override
	public int countAllDislikes() {
		return dislikeRepository.countAllDislikes();
	}

	@Override
	public Dislike toggleDislike(Long postId, Long userId) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	    Post post = postRepository.findById(postId)
	            .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
	    Optional<Dislike> existingDislikes = dislikeRepository.findDislikesByPostAndUser(postId, userId).stream().findFirst();
	    if(existingDislikes.isPresent()) {
	    	dislikeRepository.deleteById(existingDislikes.get().getId());
	    	return null;
	    }
	    else {
	    	Dislike dislike = new Dislike();
	    	dislike.setUser(user);
	    	dislike.setPost(post);
	    	dislike.setCreateAt(LocalDateTime.now());
	    	return dislikeRepository.save(dislike);
	    }
	}

	@Override
	public List<Dislike> getDislikesByPostAndUser(Long postId, Long userId) {
		return dislikeRepository.findDislikesByPostAndUser(postId, userId);
	}

	@Override
	public List<Dislike> getAllDislike() {
		return dislikeRepository.findAll();
	}

}
