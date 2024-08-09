package com.project.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Instagram.model.LikeClass;

public interface LikeClassRepository extends JpaRepository<LikeClass, Long> {
	
	@Query("SELECT l FROM LikeClass l WHERE l.user.id = :userId")
	List<LikeClass> findLikesByUser(@Param("userId") Long userId);
	
	@Query("SELECT l FROM LikeClass l WHERE l.post.id = :postId")
	List<LikeClass> findLikesByPost(@Param("postId") Long postId);
	
	@Query("SELECT COUNT(l) FROM LikeClass l WHERE l.post.id = :postId")
	int countLikesByPost(@Param("postId") Long postId);
	
	@Query("SELECT COUNT(l) FROM LikeClass l WHERE l.user.id = :userId")
	int countLikesByUser(@Param("userId") Long userId);
	
	@Query("SELECT COUNT(l) FROM LikeClass l")
	int countAllLikes();
	
	 @Query("SELECT COUNT(l) FROM LikeClass l WHERE l.user.id = :userId AND l.post.id = :postId")
	boolean userHasLikedPost(@Param("postId") Long postId,@Param("userId") Long userId);
	
	 @Query("SELECT l FROM LikeClass l WHERE l.post.id = :postId AND l.user.id = :userId")
	List<LikeClass> findLikesByPostAndUser(@Param("postId") Long postId ,@Param("userId")Long userId);
}
