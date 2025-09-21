package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Role;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.entity.UserRole;

import java.util.List;

public interface RoleService {
    Role createRole(String roleName, String description);
    Role findByRoleName(String roleName);
    List<Role> getAllRoles();
    UserRole assignRoleToUser(UserEntity user, String roleName);
    UserRole switchUserRole(UserEntity user, String roleName);
    UserRole getCurrentActiveRole(UserEntity user);
    List<UserRole> getUserRoles(UserEntity user);
    void removeRoleFromUser(UserEntity user, String roleName);
}
