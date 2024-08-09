package com.project.Instagram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.project.Instagram.model.MessageClass;

public interface MessageClassRepository extends JpaRepository<MessageClass, Long> {

	List<MessageClass> findBySenderId(Long senderId);
	
	List<MessageClass> findByReceiverId(Long receiverId);
	
	List<MessageClass> findBySenderIdAndReceiverId( Long senderId, Long receiverId);
	
	List<MessageClass> findByReceiverIdAndIsReadFalse(Long receiverId);
	
	int countByReceiverIdAndIsReadFalse(Long receiverId);
	
	@Query("SELECT m FROM MessageClass m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
	List<MessageClass> findMessageByUser(@Param("userId") Long userId);
	
	@Modifying
	@Transactional
	@Query("UPDATE MessageClass m SET m.isRead = TRUE WHERE m.id = :messageId")
	void markAsRead(@Param("messageId") Long messageId);
	
	@Query("SELECT COUNT(m) FROM MessageClass m WHERE m.receiver.id = :receiverId AND m.isRead = FALSE")
	int countUnreadMessagesByReceiver(@Param("receiverId") Long receiverId);
}
