package com.Prasad.Jobsy2.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Prasad.Jobsy2.dto.FreelancerProfileDTO;
import com.Prasad.Jobsy2.entity.FreelancerProfileEntity;
import com.Prasad.Jobsy2.entity.ProfileEntity;
import com.Prasad.Jobsy2.repository.FreelancerProfileRepository;
import com.Prasad.Jobsy2.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreelancerProfileService {

    private final FreelancerProfileRepository freelancerRepository;
    private final ProfileRepository profileRepository;

    // Get current logged-in ProfileEntity
    public ProfileEntity getCurrentProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Profile not found with email: "
                                + authentication.getName()));
    }

    // Create or Update freelancer profile
    public FreelancerProfileDTO createOrUpdateProfile(FreelancerProfileDTO dto) {

        ProfileEntity currentUser = getCurrentProfile();

        FreelancerProfileEntity entity =
                freelancerRepository.findByUserId(currentUser.getId())
                .orElse(
                    FreelancerProfileEntity.builder()
                        .user(currentUser)
                        .build()
                );

        entity.setBio(dto.getBio());
        entity.setSkills(dto.getSkills());

        entity = freelancerRepository.save(entity);

        return toDTO(entity);
    }

    // Get current freelancer profile
    public FreelancerProfileDTO getMyFreelancerProfile() {

        ProfileEntity currentUser = getCurrentProfile();

        FreelancerProfileEntity entity =
                freelancerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("Freelancer profile not found"));

        return toDTO(entity);
    }

    // Convert Entity → DTO
    public FreelancerProfileDTO toDTO(FreelancerProfileEntity entity) {

        return FreelancerProfileDTO.builder()
                .userId(entity.getUserId())
                .fullName(entity.getUser().getFullName())
                .profileImageUrl(entity.getUser().getProfileImageUrl())
                .bio(entity.getBio())
                .skills(entity.getSkills())
                .rating(entity.getRating())
                .reviewCount(entity.getReviewCount())
                .totalEarnings(entity.getTotalEarnings())
                .build();
    }

    // Convert DTO → Entity (optional utility)
    public FreelancerProfileEntity toEntity(FreelancerProfileDTO dto) {

        ProfileEntity currentUser = getCurrentProfile();

        return FreelancerProfileEntity.builder()
                .user(currentUser)
                .bio(dto.getBio())
                .skills(dto.getSkills())
                .build();
    }

    // Check if current user has freelancer profile
    public boolean hasFreelancerProfile() {

    ProfileEntity currentUser = getCurrentProfile();

    return freelancerRepository.findById(currentUser.getId()).isPresent();
}
}
