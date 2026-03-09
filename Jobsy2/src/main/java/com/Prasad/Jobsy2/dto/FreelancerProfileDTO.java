package com.Prasad.Jobsy2.dto;

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
public class FreelancerProfileDTO {

    // Display only
    @JsonProperty(access = Access.READ_ONLY)
    private Long userId;

    @JsonProperty(access = Access.READ_ONLY)
    private String fullName;

    @JsonProperty(access = Access.READ_ONLY)
    private String profileImageUrl;

    // Editable by freelancer
    private String bio;
    private String skills;

    // System controlled (read-only)
    @JsonProperty(access = Access.READ_ONLY)
    private Double rating;

    @JsonProperty(access = Access.READ_ONLY)
    private Integer reviewCount;

    @JsonProperty(access = Access.READ_ONLY)
    private Double totalEarnings;
}

