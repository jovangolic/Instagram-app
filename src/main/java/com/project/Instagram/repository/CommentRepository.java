package com.project.Instagram.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Instagram.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
	List<Comment> findCommentsByUser(@Param("userId") Long userId);
	
	@Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
	List<Comment> findCommentsByPost(@Param("postId") Long postId);
	
	@Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId")
	int countCommenstByUser(@Param("userId") Long userId);
	
	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.post.id = :postId")
	List<Comment> findCommentsByUserAndPost(@Param("userId") Long userId,@Param("postId") Long postId);
	
	@Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
	int countCommentsForPost(@Param("postId") Long postId);
	
	@Query("SELECT c FROM Comment c WHERE c.text LIKE %:text%")
	List<Comment> findCommentsByText(@Param("text") String text);
	
	@Query("SELECT c FROM Comment c WHERE c.createdAt > :date")
	List<Comment> findCommentsAfterDate(@Param("date") LocalDateTime date);
	
	@Query("SELECT c FROM Comment c WHERE c.text LIKE %:text%")
	List<Comment> findCommentsContainingText(@Param("text") String text);
	
	@Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId AND c.isRead = FALSE")
	int countUnreadCommentsByUser(@Param("userId") Long userId);
}
