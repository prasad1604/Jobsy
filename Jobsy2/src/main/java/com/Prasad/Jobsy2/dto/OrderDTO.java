package com.Prasad.Jobsy2.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.Prasad.Jobsy2.entity.OrderStatus;
import com.Prasad.Jobsy2.entity.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    // 🔒 Read-only
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    // 🔒 Read-only (derived)
    @JsonProperty(access = Access.READ_ONLY)
    private String gigTitle;

    @JsonProperty(access = Access.READ_ONLY)
    private String hirerName;

    @JsonProperty(access = Access.READ_ONLY)
    private String freelancerName;

    // 💰 Editable (at creation)
    private BigDecimal amount;

    // 🔒 System controlled
    @JsonProperty(access = Access.READ_ONLY)
    private BigDecimal platformFee;

    // 🔄 Status (read-only for safety)
    @JsonProperty(access = Access.READ_ONLY)
    private OrderStatus status;

    @JsonProperty(access = Access.READ_ONLY)
    private PaymentStatus paymentStatus;

    // 📝 Input from hirer
    private String requirements;

    // 📎 Delivery (freelancer sets)
    private String deliveryUrl;

    // 🔒 System controlled
    @JsonProperty(access = Access.READ_ONLY)
    private Integer revisionCount;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDateTime submittedAt;

    // ⏱ Deadline (can be set initially)
    private LocalDateTime deadline;

    // ❌ Cancel reason
    private String cancelReason;

    // 🔒 System controlled
    @JsonProperty(access = Access.READ_ONLY)
    private Boolean isReviewed;

    // 🕒 Timestamps
    @JsonProperty(access = Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDateTime updatedAt;
}