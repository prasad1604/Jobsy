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
@Table(name = "tbl_jobs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String jobId;
    
    @ManyToOne
    @JoinColumn(name = "hirer_id", nullable = false)
    private UserEntity hirer;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String category;
    
    @Column(name = "required_skills")
    private String requiredSkills;
    
    @Enumerated(EnumType.STRING)
    private JobType jobType; // FIXED, HOURLY
    
    private BigDecimal budget;
    
    @Enumerated(EnumType.STRING)
    private JobStatus status; // OPEN, IN_PROGRESS, COMPLETED, CANCELLED
    
    private Timestamp deadline;
    
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    
    @UpdateTimestamp
    private Timestamp updatedAt;
    
    public enum JobType {
        FIXED, HOURLY
    }
    
    public enum JobStatus {
        OPEN, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
