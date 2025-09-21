package com.prasad.Jobsy.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    
    private Long jobId; // Optional: if message is related to a job
    
    @NotBlank(message = "Message content is required")
    private String content;
}
