package com.prasad.Jobsy.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {
    
    private Long id;
    private String userId;
    private String userName;
    private String roleName;
    private Boolean isActive;
    private Timestamp createdAt;
}
