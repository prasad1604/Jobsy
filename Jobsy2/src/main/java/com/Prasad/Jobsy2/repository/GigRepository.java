package com.Prasad.Jobsy2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Prasad.Jobsy2.entity.GigEntity;
import com.Prasad.Jobsy2.entity.ProfileEntity;

@Repository
public interface GigRepository extends JpaRepository<GigEntity, Long> {

    // Get all gigs created by a freelancer
    List<GigEntity> findByFreelancer(ProfileEntity freelancer);

    // Get all active gigs
    List<GigEntity> findByIsActiveTrue();

    // Get all active gigs by freelancer
    List<GigEntity> findByFreelancerAndIsActiveTrue(ProfileEntity freelancer);

    // Get gigs by category
    List<GigEntity> findByCategoryAndIsActiveTrue(String category);

    Optional<GigEntity> findByIdAndFreelancer(Long id, ProfileEntity freelancer);

}
