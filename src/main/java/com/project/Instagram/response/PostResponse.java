package com.project.Instagram.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostResponse {

	private Long id;
	
	private UserResponse userResponse;
	
	private String caption;
	
    private String imageUrl;
    
    private String videoUrl;
    
    private String location;
    
    private List<Long> tagIds;
    
    private List<Long> likeIds;
    
    private List<Long> commentIds;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public PostResponse(Long id,UserResponse userResponse, String caption, String imageUrl, String videoUrl, String location, LocalDateTime createdAt) {
    	this.id = id;
    	this.userResponse = userResponse;
    	this.caption = caption;
    	this.imageUrl = imageUrl;
    	this.videoUrl = videoUrl;
    	this.location = location;
    	this.createdAt = createdAt;
    }
    
    public PostResponse(Long id,UserResponse userResponse, String caption, String imageUrl, String videoUrl, String location, LocalDateTime createdAt, LocalDateTime updatedAt) {
    	this.id = id;
    	this.userResponse = userResponse;
    	this.caption = caption;
    	this.imageUrl = imageUrl;
    	this.videoUrl = videoUrl;
    	this.location = location;
    	this.createdAt = createdAt;
    	this.updatedAt = updatedAt;
    }
}
