package com.prasad.Jobsy.repository;

import com.prasad.Jobsy.entity.UserRole;
import com.prasad.Jobsy.entity.UserEntity;
import com.prasad.Jobsy.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(UserEntity user);
    Optional<UserRole> findByUserAndIsActiveTrue(UserEntity user);
    Optional<UserRole> findByUserAndRole(UserEntity user, Role role);
    Boolean existsByUserAndRole(UserEntity user, Role role);
}
