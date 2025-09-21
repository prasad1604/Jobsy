package com.prasad.Jobsy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "tbl_messages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String messageId;
    
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver;
    
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job; // Optional: if message is related to a specific job
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Builder.Default
    private Boolean isRead = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
}
