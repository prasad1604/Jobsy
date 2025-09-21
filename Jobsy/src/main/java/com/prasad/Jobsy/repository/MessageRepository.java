package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.Message;
import com.prasad.Jobsy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageId(String messageId);
    
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt")
    List<Message> findConversation(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);
    
    List<Message> findByReceiverAndIsReadFalse(UserEntity receiver);
    
    List<Message> findBySenderOrReceiver(UserEntity sender, UserEntity receiver);
}
