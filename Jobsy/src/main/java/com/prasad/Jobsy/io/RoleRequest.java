package com.prasad.Jobsy.io;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {
    
    @NotBlank(message = "Role name is required")
    private String roleName;
    
    private String description;
}
