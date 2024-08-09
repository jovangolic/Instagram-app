package com.project.Instagram.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageRequest {
	
    private Long senderId;
    
    private Long receiverId;
    
    private String text;
}