package com.project.Instagram.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationRequest {

	private Long userId;
	
	private String message;
	
	//private Boolean isRead;
}
