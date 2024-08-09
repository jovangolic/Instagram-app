package com.project.Instagram.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.Instagram.model.MessageClass;
import com.project.Instagram.model.User;
import com.project.Instagram.repository.MessageClassRepository;
import com.project.Instagram.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

	private final MessageClassRepository messageRepository;
	private final UserRepository userRepository;

	@Override
	public MessageClass createMessage(User sender, User receiver, String text) {
		MessageClass message = new MessageClass();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setText(text);
		message.setIsRead(false);
		return messageRepository.save(message);
	}
	@Override
	public MessageClass createMessage(Long senderId, Long receiverId, String text) {
		User sender = userRepository.findById(senderId).orElseThrow(
				() -> new IllegalArgumentException("Sender not found"));
		User receiver = userRepository.findById(receiverId).orElseThrow(
				() -> new IllegalArgumentException("Receiver not found"));
		MessageClass msg = new MessageClass();
		msg.setSender(sender);
		msg.setReceiver(receiver);
		msg.setCreatedAt(LocalDateTime.now());
		msg.setText(text);
		msg.setIsRead(false);
		return messageRepository.save(msg);
	}

	@Override
	public void deleteMessage(Long messageId) {
		MessageClass message = messageRepository.findById(messageId).orElseThrow(
				() -> new IllegalArgumentException("Message not found with id: " + messageId));
		if(message != null) {
			messageRepository.deleteById(messageId);
		}
	}

	@Override
	public Optional<MessageClass> getMessageById(Long messageId) {
		return messageRepository.findById(messageId);
	}

	@Override
	public List<MessageClass> getAllMessages() {
		return messageRepository.findAll();
	}

	@Override
	public List<MessageClass> getMessageBySender(Long senderId) {
		return messageRepository.findBySenderId(senderId);
	}

	@Override
	public List<MessageClass> getMessageByReceiver(Long receiverId) {
		return messageRepository.findByReceiverId(receiverId);
	}

	@Override
	public List<MessageClass> getMessageBetweenUsers(Long senderId, Long receiverId) {
		return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
	}

	@Override
	public void markMessageAsRead(Long messageId) {
		messageRepository.markAsRead(messageId);
	}

	@Override
	public int countUnreadMessagesByReceiver(Long receiverId) {
		return messageRepository.countByReceiverIdAndIsReadFalse(receiverId);
	}

}
