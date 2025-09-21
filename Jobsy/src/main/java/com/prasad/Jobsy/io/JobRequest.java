package com.prasad.Jobsy.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequest {
    
    @NotBlank(message = "Job title is required")
    private String title;
    
    @NotBlank(message = "Job description is required")
    private String description;
    
    private String category;
    
    private String requiredSkills;
    
    @NotNull(message = "Job type is required")
    private String jobType; // FIXED, HOURLY
    
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;
    
    private Timestamp deadline;
}
