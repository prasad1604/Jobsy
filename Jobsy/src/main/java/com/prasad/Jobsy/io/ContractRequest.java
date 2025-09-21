package com.prasad.Jobsy.io;

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
public class ContractRequest {
    
    @NotNull(message = "Job ID is required")
    private Long jobId;
    
    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;
    
    @Positive(message = "Agreed amount must be positive")
    private BigDecimal agreedAmount;
    
    private String terms;
    
    private Timestamp startDate;
    private Timestamp endDate;
}
