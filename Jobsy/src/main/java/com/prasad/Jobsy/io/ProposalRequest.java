package com.prasad.Jobsy.io;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProposalRequest {
    
    @NotNull(message = "Job ID is required")
    private Long jobId;
    
    private String coverLetter;
    
    @Positive(message = "Bid amount must be positive")
    private BigDecimal bidAmount;
    
    @Positive(message = "Delivery time must be positive")
    private Integer deliveryTime; // in days
}
