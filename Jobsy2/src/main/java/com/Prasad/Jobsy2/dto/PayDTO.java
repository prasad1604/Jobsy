package com.Prasad.Jobsy2.dto;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayDTO {

    private Long id;

    // 🔗 Only send orderId (NOT full OrderEntity)
    private Long orderId;

    private Long payerId;

    private Double amount;
    private Double platformFee;
    private Double freelancerAmount;

    private String status;   // ESCROW / RELEASED / REFUNDED
    private String method;   // DUMMY / RAZORPAY

    private String transactionId;

    private LocalDateTime createdAt;
}