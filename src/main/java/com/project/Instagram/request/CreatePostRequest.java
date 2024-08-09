package com.project.Instagram.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//DTO za unos podataka
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePostRequest {
    @NotBlank
    private String caption;

    private String imageUrl;
    
    private String videoUrl;

    private String location;
    
    @NotNull
    private LocalDateTime createdAt;
}