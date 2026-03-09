package com.Prasad.Jobsy2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Prasad.Jobsy2.entity.FreelancerProfileEntity;

@Repository
public interface FreelancerProfileRepository 
        extends JpaRepository<FreelancerProfileEntity, Long> {

    Optional<FreelancerProfileEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}