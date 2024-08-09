package com.project.Instagram.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagResponse {

	private Long id;
	
	private String name;
	
	private List<Long> postIds;
	
	public TagResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
