package com.prasad.Jobsy.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    
    private Long id;
    private String messageId;
    private String senderName;
    private String senderEmail;
    private String receiverName;
    private String receiverEmail;
    private String jobTitle; // if related to a job
    private String content;
    private Boolean isRead;
    private Timestamp createdAt;
}
