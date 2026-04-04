package com.Prasad.Jobsy2.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 RELATIONS INSTEAD OF IDs

    // Gig reference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gig_id")
    private GigEntity gig;

    // Hirer (client)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hirer_id")
    private ProfileEntity hirer;

    // Freelancer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "freelancer_id")
    private ProfileEntity freelancer;

    // 💰 Money
    private BigDecimal amount;
    private BigDecimal platformFee;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // 📝 Requirements from hirer
    @Column(columnDefinition = "TEXT")
    private String requirements;

    // 📎 Delivery
    private String deliveryUrl;
    private LocalDateTime submittedAt;

    @Builder.Default
    private Integer revisionCount = 0;

    // ⏱ Timeline
    private LocalDateTime deadline;

    private String cancelReason;

    @Builder.Default
    private Boolean isReviewed = false;

    // 🕒 Timestamps
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}