package com.Prasad.Jobsy2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Link to Order (1 payment per order)
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    // 👤 Who paid (hirer id)
    private Long payerId;

    // 💰 Total amount
    private Double amount;

    // 💸 Platform fee
    private Double platformFee;

    // 💵 Freelancer gets this
    private Double freelancerAmount;

    // 📊 Payment status
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // 🧾 Payment method
    private String method; // DUMMY / RAZORPAY / STRIPE

    // 🆔 Transaction ID
    private String transactionId;

    // ⏱ Timestamp
    private LocalDateTime createdAt;

    // ✅ Auto-set timestamp
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}