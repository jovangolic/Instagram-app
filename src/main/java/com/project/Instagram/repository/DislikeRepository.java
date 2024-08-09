package com.project.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Instagram.model.Dislike;


public interface DislikeRepository extends JpaRepository<Dislike, Long> {

	@Query("SELECT dl FROM Dislike dl WHERE dl.user.id = :userId")
	List<Dislike> findDislikesByUser(@Param("userId") Long userId);
	
	@Query("SELECT dl FROM Dislike dl WHERE dl.post.id = :postId")
	List<Dislike> findDislikesByPost(@Param("postId") Long postId);
	
	@Query("SELECT COUNT(dl) FROM Dislike dl WHERE dl.post.id = :postId")
	int countDislikesByPost(@Param("postId") Long postId);
	
	@Query("SELECT COUNT(dl) FROM Dislike dl WHERE dl.user.id = :userId")
	int countDislikesByUser(@Param("userId") Long userId);
	
	@Query("SELECT COUNT(dl) FROM Dislike dl")
	int countAllDislikes();
	
	 @Query("SELECT COUNT(dl) FROM Dislike dl WHERE dl.user.id = :userId AND dl.post.id = :postId")
	boolean userHasDislikedPost(@Param("postId") Long postId,@Param("userId") Long userId);
	
	 @Query("SELECT dl FROM Dislike dl WHERE dl.post.id = :postId AND dl.user.id = :userId")
	List<Dislike> findDislikesByPostAndUser(@Param("postId") Long postId ,@Param("userId")Long userId);
}

