package com.project.Instagram.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageClass {

	//for private messaging between users
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="sender_id")
		private User sender;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="receiver_id")
		private User receiver;
		
		private String text;
		
		private LocalDateTime createdAt;
		
		private Boolean isRead;
		
		@PrePersist
		protected void onCreate() {
			createdAt = LocalDateTime.now();
		}
}
