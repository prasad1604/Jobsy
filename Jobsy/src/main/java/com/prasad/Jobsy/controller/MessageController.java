package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Message;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message sendMessage(
            @Valid @RequestBody Message message,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        message.setSender(sender);
        return messageService.sendMessage(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<Message>> getConversation(
            @PathVariable Long userId,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        UserEntity otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Other user not found"));
        
        List<Message> conversation = messageService.getConversation(currentUser, otherUser);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Message>> getUnreadMessages(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Message> unreadMessages = messageService.getUnreadMessages(user);
        return ResponseEntity.ok(unreadMessages);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markMessageAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/mark-all-read/{userId}")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long userId,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        UserEntity otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Other user not found"));
        
        messageService.markAllMessagesAsRead(otherUser, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-messages")
    public ResponseEntity<List<Message>> getMyMessages(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<Message> messages = messageService.getUserMessages(user);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        Message message = messageService.getMessageById(id);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (!message.getSender().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own messages");
        }
        
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
