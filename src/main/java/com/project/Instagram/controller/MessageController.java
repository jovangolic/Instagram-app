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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.project.Instagram.exception.ErrorCreatingMessageException;
import com.project.Instagram.model.MessageClass;
import com.project.Instagram.repository.UserRepository;
import com.project.Instagram.request.MessageRequest;
import com.project.Instagram.response.MessageResponse;
import com.project.Instagram.service.IMessageService;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@CrossOrigin("")
@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

	private final IMessageService messageService;
	private final UserRepository userRepository;
	
	/*@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/add/new-message")
	public ResponseEntity<Object> createMessage(
			@RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String text){
		try {
			User sender = userRepository.findById(senderId).orElseThrow(
					() -> new IllegalArgumentException("Sender not found"));
			User receiver = userRepository.findById(receiverId).orElseThrow(
					() -> new IllegalArgumentException("Receiver not found"));
			MessageClass msg = messageService.createMessage(sender, receiver, text);
			MessageResponse response = new MessageResponse(msg.getId(),
					senderId,
					receiverId,
					msg.getText(),
					msg.getCreatedAt());
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingMessageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}*/
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/add/new-message")
	public ResponseEntity<Object> createMessage(@RequestBody MessageRequest request){
		try {
			MessageClass msg = messageService.createMessage(request.getSenderId(), request.getReceiverId(), request.getText());
			MessageResponse response = new MessageResponse(msg.getId(),
					request.getSenderId(),
					request.getReceiverId(),
					msg.getText(),
					msg.getCreatedAt());
			return ResponseEntity.ok(response);
		}
		catch(ErrorCreatingMessageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@DeleteMapping("/delete/message/{messageId}")
	public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId){
		messageService.deleteMessage(messageId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{messageId}")
	public ResponseEntity<Object> getMessageById(@PathVariable Long messageId){
		try {
			MessageClass msg = messageService.getMessageById(messageId)
	                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + messageId));
	        return ResponseEntity.ok(msg);
		}
		catch(ErrorCreatingMessageException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/all-messages")
	public ResponseEntity<List<MessageResponse>> getAllMessages(){
		List<MessageClass> msgs = messageService.getAllMessages();
		List<MessageResponse> responses = msgs.stream().map(
				this::getMessageResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/sender/{senderId}")
	public ResponseEntity<List<MessageResponse>> getMessageBySender(@PathVariable Long senderId){
		List<MessageClass> msgs = messageService.getMessageBySender(senderId);
		List<MessageResponse> responses = msgs.stream().map(
				this::getMessageResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/receiver/{receiverId}")
	public ResponseEntity<List<MessageResponse>> getMessageByReceiver(@PathVariable Long receiverId){
		List<MessageClass> msgs = messageService.getMessageByReceiver(receiverId);
		List<MessageResponse> responses = msgs.stream().map(
				this::getMessageResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/between-users")
	public ResponseEntity<List<MessageResponse>> getMessageBetweenUsers(@RequestParam Long senderId,@RequestParam Long receiverId){
		List<MessageClass> msgs = messageService.getMessageBetweenUsers(senderId, receiverId);
		List<MessageResponse> responses = msgs.stream().map(
				this::getMessageResponse).collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/{messageId}/mark-as-read")
	public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId){
		messageService.markMessageAsRead(messageId);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/unread-count")
	public ResponseEntity<Integer> countUnreadMessagesByReceiver(@RequestParam Long receiverId){
		int countUnRead = messageService.countUnreadMessagesByReceiver(receiverId);
		return ResponseEntity.ok(countUnRead);
	}
	
	private MessageResponse getMessageResponse(MessageClass msg) {
		return new MessageResponse(msg.getId(),
				msg.getSender().getId(),
				msg.getReceiver().getId(),
				msg.getText(),
				msg.getIsRead());
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
