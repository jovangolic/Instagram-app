package com.project.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Instagram.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	
	List<Tag> findByNameContainingIgnoreCase(String name);
}
