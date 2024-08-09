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
public class MessageResponse {

	private Long id;
	
	private Long senderId;
	
	private Long receiverId;
	
	private String text;
	
	private LocalDateTime createdAt;
	
	private Boolean read;
	
	public MessageResponse(Long id, Long senderId, Long receiverId, String text, LocalDateTime createdAt) {
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.text = text;
		this.createdAt = createdAt;
	}
	
	public MessageResponse(Long id, Long senderId, Long receiverId, String text,Boolean read) {
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.text = text;
		this.read = read;
	}
}
