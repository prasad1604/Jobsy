package com.prasad.Jobsy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_payments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String paymentId;
    
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
    
    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private UserEntity payer; // Hirer
    
    @ManyToOne
    @JoinColumn(name = "payee_id", nullable = false)
    private UserEntity payee; // Freelancer
    
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, FAILED, REFUNDED
    
    @Enumerated(EnumType.STRING)
    private PaymentType type; // MILESTONE, FINAL, BONUS
    
    private String transactionId; // Gateway transaction ID
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    public enum PaymentType {
        MILESTONE, FINAL, BONUS
    }
}
