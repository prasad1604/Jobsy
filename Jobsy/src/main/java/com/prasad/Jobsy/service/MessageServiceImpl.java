package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Message;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message sendMessage(Message message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
    }

    @Override
    public Message getMessageByMessageId(String messageId) {
        return messageRepository.findByMessageId(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
    }

    @Override
    public List<Message> getConversation(UserEntity user1, UserEntity user2) {
        return messageRepository.findConversation(user1, user2);
    }

    @Override
    public List<Message> getUnreadMessages(UserEntity user) {
        return messageRepository.findByReceiverAndIsReadFalse(user);
    }

    @Override
    public void markMessageAsRead(Long messageId) {
        Message message = getMessageById(messageId);
        message.setIsRead(true);
        messageRepository.save(message);
    }

    @Override
    public void markAllMessagesAsRead(UserEntity sender, UserEntity receiver) {
        List<Message> conversation = getConversation(sender, receiver);
        conversation.forEach(message -> {
            if (message.getReceiver().equals(receiver)) {
                message.setIsRead(true);
            }
        });
        messageRepository.saveAll(conversation);
    }

    @Override
    public List<Message> getUserMessages(UserEntity user) {
        return messageRepository.findBySenderOrReceiver(user, user);
    }

    @Override
    public void deleteMessage(Long id) {
        Message message = getMessageById(id);
        messageRepository.delete(message);
    }
}
