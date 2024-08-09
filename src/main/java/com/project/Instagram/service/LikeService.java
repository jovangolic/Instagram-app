package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Instagram.model.LikeClass;
import com.project.Instagram.model.Post;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.LikeClassRepository;
import com.project.Instagram.repository.PostRepository;
import com.project.Instagram.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService implements ILikeService {

	private final LikeClassRepository likeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	
	@Override
	public LikeClass createLike(Long userId, Long postId) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	    Post post = postRepository.findById(postId)
	            .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

	    LikeClass like = new LikeClass();
	    like.setUser(user);
	    like.setPost(post);
	    like.setCreatedAt(LocalDateTime.now());
	    return likeRepository.save(like);
	}

	@Override
	public LikeClass createLike(Long userId, String username, Post post, LocalDateTime createdAt) {
		LikeClass like = new LikeClass();
		like.setUser(new User(userId, username));
		like.setPost(post);
		like.setCreatedAt(createdAt);
		return likeRepository.save(like);
	}

	@Override
	public LikeClass createLike(LikeClass like) {
		return likeRepository.save(like);
	}

	@Override
	@Transactional
	public void deleteLike(Long likeId) {
		Optional<LikeClass> like = likeRepository.findById(likeId);
		if(like.isPresent()) {
			likeRepository.deleteById(likeId);
		}
	}

	@Override
	public Optional<LikeClass> getLikeById(Long likeId) {
		return likeRepository.findById(likeId);
	}

	@Override
	public List<LikeClass> getAllLikes() {
		return likeRepository.findAll();
	}

	@Override
	public List<LikeClass> getLikesByUser(Long userId) {
		return likeRepository.findLikesByUser(userId);
	}

	@Override
	public List<LikeClass> findLikesByPost(Long postId) {
		return likeRepository.findLikesByPost(postId);
	}

	@Override
	public int countLikesByPost(Long postId) {
		return likeRepository.countLikesByPost(postId);
	}

	@Override
	public int countLikesByUser(Long userId) {
		return likeRepository.countLikesByUser(userId);
	}

	@Override
	public int countAllLikes() {
		return likeRepository.countAllLikes();
	}

	@Override
	@Transactional
	public LikeClass toggleLike(Long postId, Long userId) {
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	    Post post = postRepository.findById(postId)
	            .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));
		Optional<LikeClass> existingLike = likeRepository.findLikesByPostAndUser(postId, userId).stream().findFirst();
		if(existingLike.isPresent()) {
			likeRepository.deleteById(existingLike.get().getId());
			return null; //like je uklonjen
		}
		else {
			LikeClass newLike = new LikeClass();
			newLike.setUser(user);
			newLike.setPost(post);
			newLike.setCreatedAt(LocalDateTime.now());
			return likeRepository.save(newLike);
		}
	}
	

	@Override
	public boolean userHasLikedPost(Long postId, Long userId) {
		return likeRepository.userHasLikedPost(postId, userId);
	}

	@Override
	public List<LikeClass> getLikesByPostAndUser(Long postId, Long userId) {
		return likeRepository.findLikesByPostAndUser(postId, userId);
	}

}
