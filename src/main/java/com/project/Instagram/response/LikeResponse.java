package com.project.Instagram.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LikeResponse {

	private Long id;
	
	private Long userId;
	
	private String username;
	
	private PostResponse postResponse;
	
	private LocalDateTime createdAt;
	
	public LikeResponse(Long id, Long userId, String username, PostResponse postResponse) {
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.postResponse = postResponse;
	}
}
