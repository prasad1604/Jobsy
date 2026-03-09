package com.Prasad.Jobsy2.dto;

import com.Prasad.Jobsy2.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleSwitchDTO {
    private Role role;
}