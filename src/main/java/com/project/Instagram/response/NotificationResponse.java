package com.project.Instagram.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationResponse {

	private Long id;
	
	private String message;
	
	private Boolean read;
	
	private LocalDateTime createdAt;
	
	public NotificationResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
	
	public NotificationResponse(Long id, String message, LocalDateTime createdAt) {
		this.id = id;
		this.message = message;
		this.createdAt = createdAt;
	}
}
