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
@Table(name = "tbl_proposals")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Proposal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String proposalId;
    
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private UserEntity freelancer;
    
    @Column(columnDefinition = "TEXT")
    private String coverLetter;
    
    private BigDecimal bidAmount;
    
    private Integer deliveryTime; // in days
    
    @Enumerated(EnumType.STRING)
    private ProposalStatus status; // PENDING, ACCEPTED, REJECTED, WITHDRAWN
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    private Timestamp updatedAt;
    
    public enum ProposalStatus {
        PENDING, ACCEPTED, REJECTED, WITHDRAWN
    }
}
