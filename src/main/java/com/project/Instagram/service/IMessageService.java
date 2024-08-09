package com.project.Instagram.service;

import java.util.List;
import java.util.Optional;

import com.project.Instagram.model.MessageClass;
import com.project.Instagram.model.User;

public interface IMessageService {

	MessageClass createMessage(User sender, User receiver, String text);
	
	MessageClass createMessage(Long senderId,Long receiverId,String text);
	
	void deleteMessage(Long messageId);
	
	Optional<MessageClass> getMessageById(Long messageId);
	
	List<MessageClass> getAllMessages();
	
	List<MessageClass> getMessageBySender(Long senderId);
	
	List<MessageClass> getMessageByReceiver(Long receiverId);
	
	List<MessageClass> getMessageBetweenUsers(Long senderId, Long receiverId);
	
	void markMessageAsRead(Long messageId);
	
	int countUnreadMessagesByReceiver(Long receiverId);
}
