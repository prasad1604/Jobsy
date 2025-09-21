package com.prasad.Jobsy.controller;

import com.prasad.Jobsy.entity.Role;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.entity.UserRole;
import com.prasad.Jobsy.repository.UserRepository;
import com.prasad.Jobsy.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final UserRepository userRepository;

    @PostMapping("/assign")
    public ResponseEntity<UserRole> assignRole(
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        String roleName = request.get("roleName");
        UserRole userRole = roleService.assignRoleToUser(user, roleName);
        return ResponseEntity.ok(userRole);
    }

    @PostMapping("/switch")
    public ResponseEntity<UserRole> switchRole(
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        String roleName = request.get("roleName");
        UserRole userRole = roleService.switchUserRole(user, roleName);
        return ResponseEntity.ok(userRole);
    }

    @GetMapping("/current")
    public ResponseEntity<UserRole> getCurrentRole(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        UserRole currentRole = roleService.getCurrentActiveRole(user);
        return ResponseEntity.ok(currentRole);
    }

    @GetMapping("/my-roles")
    public ResponseEntity<List<UserRole>> getUserRoles(
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        List<UserRole> userRoles = roleService.getUserRoles(user);
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeRole(
            @RequestBody Map<String, String> request,
            @CurrentSecurityContext(expression = "authentication?.name") String email) {
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        String roleName = request.get("roleName");
        roleService.removeRoleFromUser(user, roleName);
        return ResponseEntity.noContent().build();
    }
}
