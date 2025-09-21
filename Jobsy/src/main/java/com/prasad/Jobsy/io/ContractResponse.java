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
public class ContractResponse {
    
    private Long id;
    private String contractId;
    private String jobTitle;
    private String jobId;
    private String freelancerName;
    private String freelancerEmail;
    private String hirerName;
    private String hirerEmail;
    private BigDecimal agreedAmount;
    private String terms;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
