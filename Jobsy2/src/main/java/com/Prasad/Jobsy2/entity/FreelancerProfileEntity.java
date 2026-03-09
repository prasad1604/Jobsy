package com.Prasad.Jobsy2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "freelancer_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreelancerProfileEntity {

    // Same as tbl_profiles.id
    @Id
    private Long userId;

    // Foreign key → tbl_profiles.id
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "fk_freelancer_profile_user")
    )
    private ProfileEntity user;

    // Editable by freelancer
    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 1000)
    private String skills;

    // System controlled (DO NOT expose in request DTO)
    @Builder.Default
    @Column(nullable = false)
    private Double rating = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private Integer reviewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Double totalEarnings = 0.0;
}