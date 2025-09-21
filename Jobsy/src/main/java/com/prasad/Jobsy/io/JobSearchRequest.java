package com.prasad.Jobsy.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobSearchRequest {
    
    private String keyword;
    private String category;
    private String jobType;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private String status;
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private String sortBy = "createdAt";
    @Builder.Default
    private String sortDirection = "desc";
}
