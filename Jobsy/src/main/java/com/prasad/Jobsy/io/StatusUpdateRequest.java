package com.prasad.Jobsy.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusUpdateRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String reason; // Optional reason for status change
}
