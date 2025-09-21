package com.prasad.Jobsy.io;

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
public class JobResponse {
    
    private Long id;
    private String jobId;
    private String hirerName;
    private String hirerEmail;
    private String title;
    private String description;
    private String category;
    private String requiredSkills;
    private String jobType;
    private BigDecimal budget;
    private String status;
    private Timestamp deadline;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer proposalCount;
}
