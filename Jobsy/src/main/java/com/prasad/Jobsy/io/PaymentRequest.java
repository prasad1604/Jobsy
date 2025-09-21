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
public class PaymentRequest {
    
    @NotNull(message = "Contract ID is required")
    private Long contractId;
    
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Payment type is required")
    private String type; // MILESTONE, FINAL, BONUS
    
    private String transactionId; // Gateway transaction ID
}
