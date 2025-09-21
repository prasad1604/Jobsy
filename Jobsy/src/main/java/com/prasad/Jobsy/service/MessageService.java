package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Message;
import com.prasad.Jobsy.entity.UserEntity;

import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    Message getMessageById(Long id);
    Message getMessageByMessageId(String messageId);
    List<Message> getConversation(UserEntity user1, UserEntity user2);
    List<Message> getUnreadMessages(UserEntity user);
    void markMessageAsRead(Long messageId);
    void markAllMessagesAsRead(UserEntity sender, UserEntity receiver);
    List<Message> getUserMessages(UserEntity user);
    void deleteMessage(Long id);
}
