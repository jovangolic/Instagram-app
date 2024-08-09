package com.project.Instagram.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class DislikeResponse {

	private Long id;
    private Long userId;
    private String username;
    private Long postId;
    private LocalDateTime createdAt;

    public DislikeResponse(Long id, Long userId, String username, Long postId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.postId = postId;
        this.createdAt = createdAt;
    }
	
}
