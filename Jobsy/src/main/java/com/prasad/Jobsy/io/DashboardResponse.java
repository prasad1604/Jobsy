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
public class DashboardResponse {
    
    // Common stats
    private Long totalJobs;
    private Long totalProposals;
    private Long totalContracts;
    private Long unreadMessages;
    
    // Role-specific stats
    private String activeRole;
    
    // Freelancer-specific
    private Long appliedJobs;
    private Long activeContracts;
    private BigDecimal totalEarnings;
    
    // Hirer-specific
    private Long postedJobs;
    private Long pendingProposals;
    private BigDecimal totalSpent;
}
