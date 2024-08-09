package com.project.Instagram.service;

import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.NotificationClass;

public interface INotificationService {

	NotificationClass createNotification(Long userId ,String message);
	
	void deleteNotification(Long notificationId);
	
	Optional<NotificationClass> getNotificationById(Long notificationId);
	
	List<NotificationClass> getALLNotification();
	
	List<NotificationClass> getNotificationsByUser(Long userId);
	
	int countUnreadNotificationsByUser(Long userId);
	
	void markNotificationAsRead(Long notificationId);
}
