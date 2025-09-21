package com.prasad.Jobsy.service;

import com.prasad.Jobsy.entity.Role;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.entity.UserRole;
import com.prasad.Jobsy.repository.RoleRepository;
import com.prasad.Jobsy.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Role createRole(String roleName, String description) {
        if (roleRepository.existsByRoleName(roleName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists");
        }
        
        Role role = Role.builder()
                .roleName(roleName)
                .description(description)
                .build();
        
        return roleRepository.save(role);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleName));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserRole assignRoleToUser(UserEntity user, String roleName) {
        Role role = findByRoleName(roleName);
        
        if (userRoleRepository.existsByUserAndRole(user, role)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has this role");
        }
        
        // Set all other roles to inactive
        List<UserRole> existingRoles = userRoleRepository.findByUser(user);
        existingRoles.forEach(ur -> ur.setIsActive(false));
        userRoleRepository.saveAll(existingRoles);
        
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .isActive(true)
                .build();
        
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole switchUserRole(UserEntity user, String roleName) {
        Role role = findByRoleName(roleName);
        
        Optional<UserRole> existingUserRole = userRoleRepository.findByUserAndRole(user, role);
        if (existingUserRole.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't have this role");
        }
        
        // Set all roles to inactive
        List<UserRole> allUserRoles = userRoleRepository.findByUser(user);
        allUserRoles.forEach(ur -> ur.setIsActive(false));
        userRoleRepository.saveAll(allUserRoles);
        
        // Set the requested role as active
        UserRole userRole = existingUserRole.get();
        userRole.setIsActive(true);
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole getCurrentActiveRole(UserEntity user) {
        return userRoleRepository.findByUserAndIsActiveTrue(user)
                .orElse(null);
    }

    @Override
    public List<UserRole> getUserRoles(UserEntity user) {
        return userRoleRepository.findByUser(user);
    }

    @Override
    public void removeRoleFromUser(UserEntity user, String roleName) {
        Role role = findByRoleName(roleName);
        UserRole userRole = userRoleRepository.findByUserAndRole(user, role)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't have this role"));
        
        userRoleRepository.delete(userRole);
    }
}
