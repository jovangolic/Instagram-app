package com.project.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.Instagram.model.NotificationClass;

public interface NotificationClassRepository extends JpaRepository<NotificationClass, Long> {

	@Query("SELECT n FROM NotificationClass n WHERE n.user.id = :userId")
	List<NotificationClass> findNotificationsByUser(@Param("userId") Long userId);
	
	@Query("SELECT COUNT(n) FROM NotificationClass n WHERE n.user.id = :userId AND n.isRead = false")
	int countUnreadNotificationsByUser(@Param("userId") Long userId);
	
	@Modifying
	@Transactional
	@Query("UPDATE NotificationClass n SET n.isRead = true WHERE n.id = :notificationId")
	void markAsRead(@Param("notificationId") Long notificationId);
}
