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
public class GigDTO {

    // Read-only (returned in response)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    // Editable fields (request + response)
    private String title;

    private String description;

    private Double price;

    private Integer deliveryDays;

    private String category;

    // Read-only (system controlled)
    @JsonProperty(access = Access.READ_ONLY)
    private Boolean isActive;

    // Freelancer info (read-only)
    @JsonProperty(access = Access.READ_ONLY)
    private String freelancerName;

    @JsonProperty(access = Access.READ_ONLY)
    private String freelancerProfileImage;

}
