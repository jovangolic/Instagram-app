package com.project.Instagram.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.project.Instagram.exception.ErrorCreatingTagException;
import com.project.Instagram.model.Tag;
import com.project.Instagram.response.TagResponse;
import com.project.Instagram.service.ITagService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

	
	private ITagService tagService;
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/add/new-tag")
	public ResponseEntity<Object> createTag(@RequestParam("name") String name){
		try {
			Tag savedTag = tagService.createTag(name);
			TagResponse response = getTagResponse(savedTag);
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingTagException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PutMapping("/update/{tagId}")
	public ResponseEntity<TagResponse> updateTag(@PathVariable Long tagId, @RequestParam(required = false) String name){
		Tag tag = tagService.updateTag(tagId, name);
		TagResponse response = getTagResponse(tag);
		return ResponseEntity.ok(response);
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@DeleteMapping("/delete/tag/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long tagId){
		tagService.deleteTag(tagId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/tag/{tagId}")
	public ResponseEntity<Object> getTag(@PathVariable Long tagId){
		try {
			Tag tag = tagService.getTagById(tagId).orElseThrow(
					() -> new IllegalArgumentException("Tag not found with id: " + tagId));
			TagResponse response = getTagResponse(tag);
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingTagException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/all-tags")
	public ResponseEntity<List<TagResponse>> getAllTags(){
		List<Tag> tags = tagService.getAllTags();
		List<TagResponse> responses = tags.stream().map(this::getTagResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	//metoda za pretragu
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/search/{name}")
	public ResponseEntity<List<TagResponse>> findTagsByName(@PathVariable String name){
		List<Tag> tags = tagService.findTagsByName(name);
		List<TagResponse> responses = tags.stream().map(this::getTagResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	private TagResponse getTagResponse(Tag tag) {
		return new TagResponse(tag.getId(), tag.getName());
	}
	
	//ErrorResponse class for returning error messages
		@NoArgsConstructor
		@AllArgsConstructor
		@Getter
		@Setter
		public static class ErrorResponse {
		    private String message;
		}
}
