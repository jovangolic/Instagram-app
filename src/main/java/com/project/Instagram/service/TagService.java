package com.project.Instagram.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.Instagram.exception.ResourceNotFoundException;
import com.project.Instagram.model.Tag;
import com.project.Instagram.repository.TagRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TagService implements ITagService {
	
	
	private final TagRepository tagRepository;

	@Override
	public Tag createTag(String name) {
		Tag tag = new Tag();
		tag.setName(name);
		return tagRepository.save(tag);
	}

	@Override
	public Tag updateTag(Long tagId, String name) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(
				() -> new ResourceNotFoundException("Tag not found with id: " + tagId));
		//provera da li name nije null i da li nije prazan string
		if(name != null && !name.isEmpty()) {
			tag.setName(name);
			
		}
		return tagRepository.save(tag);
	}

	@Override
	public void deleteTag(Long tagId) {
		Optional<Tag> tag = tagRepository.findById(tagId);
		if(tag.isPresent()) {
			tagRepository.deleteById(tagId);
		}
	}

	@Override
	public Optional<Tag> getTagById(Long tagId) {
		return tagRepository.findById(tagId);
	}

	@Override
	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}

	@Override
	public List<Tag> findTagsByName(String name) {
		return tagRepository.findByNameContainingIgnoreCase(name);
	}

}
