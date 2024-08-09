package com.project.Instagram.response;

import java.util.Base64;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

	private Long id;
	
    private String username;
    
    private String email;
    
    private String profilePicture;
    
    private String bio;
    
    private List<Long> followerIds;
    
    private List<Long> followingIds;
    
    private List<Long> postIds;
    
    private List<String> roles;
    
    
    public UserResponse(Long id, String username, String email,byte[] photoBytes, String bio) {
    	this.id = id;
    	this.username = username;
    	this.email = email;
    	this.profilePicture = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
    	this.bio = bio;
    }
    
    public UserResponse(Long id, String username, String email, String bio) {
    	this.id = id;
    	this.username = username;
    	this.email = email;
    	this.bio = bio;
    }
}
