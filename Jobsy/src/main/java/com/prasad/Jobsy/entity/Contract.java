package com.prasad.Jobsy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_contracts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String contractId;
    
    @OneToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private UserEntity freelancer;
    
    @ManyToOne
    @JoinColumn(name = "hirer_id", nullable = false)
    private UserEntity hirer;
    
    private BigDecimal agreedAmount;
    
    @Column(columnDefinition = "TEXT")
    private String terms;
    
    private Timestamp startDate;
    private Timestamp endDate;
    
    @Enumerated(EnumType.STRING)
    private ContractStatus status; // ACTIVE, COMPLETED, CANCELLED, DISPUTED
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    private Timestamp updatedAt;
    
    public enum ContractStatus {
        ACTIVE, COMPLETED, CANCELLED, DISPUTED
    }
}
