package com.project.Instagram.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Instagram.exception.ErrorCreatingNotificationException;
import com.project.Instagram.model.NotificationClass;
import com.project.Instagram.request.NotificationRequest;
import com.project.Instagram.response.NotificationResponse;
import com.project.Instagram.service.INotificationService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

	
	private final INotificationService notificationService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create/new-notification")
	public ResponseEntity<Object> createNotification(@RequestBody NotificationRequest request){
		try {
			NotificationClass notification = notificationService.createNotification(request.getUserId(), request.getMessage());
			NotificationResponse response = new NotificationResponse(notification.getId(),notification.getMessage(),notification.getCreatedAt());
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingNotificationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/notification/{notificationId}")
	public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId){
		notificationService.deleteNotification(notificationId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/notification/{notificationId}")
	public ResponseEntity<Object> getNotificationById(@PathVariable Long notificationId){
		try {
			NotificationClass notification = notificationService.getNotificationById(notificationId).orElseThrow(
					() -> new IllegalArgumentException("Notification not found"));
			NotificationResponse response = getNotificationResponse(notification);
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingNotificationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@GetMapping("/all-notifications")
	public ResponseEntity<List<NotificationResponse>> getAllNotifications(){
		List<NotificationClass> notifications = notificationService.getALLNotification();
		List<NotificationResponse> responses = notifications.stream().map(this::getNotificationResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/notification/{userId}")
	public ResponseEntity<List<NotificationResponse>> getNotificationsByUser(@PathVariable Long userId){
		List<NotificationClass> notifications = notificationService.getNotificationsByUser(userId);
		List<NotificationResponse> responses = notifications.stream().map(this::getNotificationResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/count-unread/{userId}")
	public ResponseEntity<Integer> countUnreadNotificationsByUser(@PathVariable Long userId){
		int count = notificationService.countUnreadNotificationsByUser(userId);
		return ResponseEntity.ok(count);
	}
	
	//anotacija postMapping se stavlja zato sto se menja stanje
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/{notificationId}/mark-as-read")
	public ResponseEntity<Object> markNotificationAsRead(@PathVariable Long notificationId){
		try {
			notificationService.markNotificationAsRead(notificationId);
			return ResponseEntity.noContent().build();
		}
		catch(ErrorCreatingNotificationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	private NotificationResponse getNotificationResponse(NotificationClass notification) {
		return new NotificationResponse(notification.getId(),
				notification.getMessage(),
				notification.getCreatedAt());
	}
	
	//ErrorResponse class for returning error messages
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class ErrorResponse {
			  private String message;
	}
}
