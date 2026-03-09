package com.Prasad.Jobsy2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Prasad.Jobsy2.dto.FreelancerProfileDTO;
import com.Prasad.Jobsy2.service.FreelancerProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/freelancer")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;

    // Create or Update freelancer profile
    @PostMapping("/profile")
    public ResponseEntity<FreelancerProfileDTO> createOrUpdateProfile(
            @RequestBody FreelancerProfileDTO freelancerProfileDTO) {

        FreelancerProfileDTO savedProfile =
                freelancerProfileService.createOrUpdateProfile(freelancerProfileDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProfile);
    }

    // Get current user's freelancer profile
    @GetMapping("/profile")
    public ResponseEntity<?> getMyFreelancerProfile() {

        try {
            FreelancerProfileDTO profile =
                    freelancerProfileService.getMyFreelancerProfile();

            return ResponseEntity.ok(profile);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Freelancer profile not found. Please create one first.");
        }
    }

    // Check if freelancer profile exists
    @GetMapping("/profile/exists")
    public ResponseEntity<Boolean> hasFreelancerProfile() {

        boolean exists =
                freelancerProfileService.hasFreelancerProfile();

        return ResponseEntity.ok(exists);
    }
}
