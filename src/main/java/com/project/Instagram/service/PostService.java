package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.Instagram.model.Post;
import com.project.Instagram.model.Tag;
import com.project.Instagram.repository.PostRepository;
import com.project.Instagram.repository.TagRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
	
	private final PostRepository postRepository;
	private final TagRepository tagRepository;

	@Override
	public Post createPost(String caption, String imageUrl, String videoUrl, String location, LocalDateTime createdAt) {
		Post post = new Post();
		post.setCaption(caption);
		post.setImageUrl(imageUrl);
		post.setVideoUrl(videoUrl);
		post.setLocation(location);
		post.setCreatedAt(createdAt);
		post.setUpdatedAt(LocalDateTime.now());
		return postRepository.save(post);
	}

	@Override
	public Post updatePost(Long postId, String caption, String imageUrl, String videoUrl, String location) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found " + postId));
		post.setCaption(caption);
		post.setImageUrl(imageUrl);
		post.setVideoUrl(videoUrl);
		post.setLocation(location);
		post.setUpdatedAt(LocalDateTime.now());
		return postRepository.save(post);
	}

	@Override
	public void deletePost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found " + postId));
		if(post != null) {
			postRepository.deleteById(postId);
		}
	}

	@Override
	public Post getOne(Long postId) {
		return postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found " + postId));
	}

	@Override
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	@Override
	public List<Post> getPostByUserId(Long userId) {
		return postRepository.findByUserId(userId);
	}

	@Override
	public List<Post> getPostByLikeId(Long likeId) {
		return postRepository.findByLikesId(likeId);
	}

	@Override
	public List<Post> getPostByCommentId(Long commentId) {
		return postRepository.findByCommentsId(commentId);
	}

	@Override
	public List<Post> getPostsByTagId(Long tagId) {
		return postRepository.findByTagId(tagId);
	}

	@Override
	public List<Post> getPostByDateRange(LocalDateTime startDate, LocalDateTime endtDate) {
		return postRepository.findByCreatedAtBetween(startDate, endtDate);
	}

	@Override
	public List<Post> getPostsSorted(String sortBy, String sortDirection) {
		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
		return postRepository.findAll(sort);
	}

	@Override
	public int getLikeCount(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found " + postId));
		return post.getLikes().size();
	}

	@Override
	public int getCommentCount(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found "+ postId));
		return post.getComments().size();
	}

	@Override
	public Post addTagToPost(Long postId, Long tagId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found "+ postId));
		Tag tag = tagRepository.findById(tagId).orElseThrow(
				() -> new IllegalArgumentException("Tag not found " + tagId));
		post.getTags().add(tag);
		return postRepository.save(post);
	}

	@Override
	public Post removeTagFromPost(Long postId, Long tagId) {
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new IllegalArgumentException("Post not found "+ postId));
		Tag tag = tagRepository.findById(tagId).orElseThrow(
				() -> new IllegalArgumentException("Tag not found " + tagId));
		post.getTags().remove(tag);
		return postRepository.save(post);
	}

}
