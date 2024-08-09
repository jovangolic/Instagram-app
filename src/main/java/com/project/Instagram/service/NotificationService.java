package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.Instagram.model.NotificationClass;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.NotificationClassRepository;
import com.project.Instagram.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
	
	private final NotificationClassRepository notificationRepository;
	private final UserRepository userRepository;

	@Override
	public NotificationClass createNotification(Long userId, String message) {
		User user = userRepository.findById(userId).orElseThrow(
				() -> new IllegalArgumentException("User not found"));
		NotificationClass notification = new NotificationClass();
		notification.setUser(user);
		notification.setMessage(message);
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		return notificationRepository.save(notification);
	}

	@Override
	public void deleteNotification(Long notificationId) {
		NotificationClass notification = notificationRepository.findById(notificationId).orElseThrow(
				() -> new IllegalArgumentException("Notification not found"));
		if(notification != null) {
			notificationRepository.deleteById(notificationId);
		}
	}

	@Override
	public Optional<NotificationClass> getNotificationById(Long notificationId) {
		return notificationRepository.findById(notificationId);
	}

	@Override
	public List<NotificationClass> getALLNotification() {
		return notificationRepository.findAll();
	}

	@Override
	public List<NotificationClass> getNotificationsByUser(Long userId) {
		return notificationRepository.findNotificationsByUser(userId);
	}

	@Override
	public int countUnreadNotificationsByUser(Long userId) {
		return notificationRepository.countUnreadNotificationsByUser(userId);
	}

	@Override
	public void markNotificationAsRead(Long notificationId) {
		notificationRepository.markAsRead(notificationId);
	}
}
