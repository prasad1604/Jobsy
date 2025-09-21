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
public class PaymentResponse {
    
    private Long id;
    private String paymentId;
    private String contractId;
    private String jobTitle;
    private String payerName;
    private String payerEmail;
    private String payeeName;
    private String payeeEmail;
    private BigDecimal amount;
    private String status;
    private String type;
    private String transactionId;
    private Timestamp createdAt;
}
