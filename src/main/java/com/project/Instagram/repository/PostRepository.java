package com.project.Instagram.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.Instagram.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserId(Long userId);

    List<Post> findByLikesId(Long likeId);

    List<Post> findByCommentsId(Long commentId);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId ")
    List<Post> findByTagId(@Param("tagId") Long tagId);

    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
