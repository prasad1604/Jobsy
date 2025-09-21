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
public class ProposalResponse {
    
    private Long id;
    private String proposalId;
    private String jobTitle;
    private String jobId;
    private String freelancerName;
    private String freelancerEmail;
    private String coverLetter;
    private BigDecimal bidAmount;
    private Integer deliveryTime;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
