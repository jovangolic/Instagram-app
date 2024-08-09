package com.project.Instagram.service;

import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.Tag;

public interface ITagService {

	Tag createTag(String name);
	
	Tag updateTag(Long tagId, String name);
	
	void deleteTag(Long tagId);
	
	Optional<Tag> getTagById(Long tagId);
	
	List<Tag> getAllTags();
	
	List<Tag> findTagsByName(String name);
}
