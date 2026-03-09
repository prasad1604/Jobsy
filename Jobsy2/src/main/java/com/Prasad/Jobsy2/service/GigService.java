package com.Prasad.Jobsy2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Prasad.Jobsy2.dto.GigDTO;
import com.Prasad.Jobsy2.entity.GigEntity;
import com.Prasad.Jobsy2.entity.ProfileEntity;
import com.Prasad.Jobsy2.repository.FreelancerProfileRepository;
import com.Prasad.Jobsy2.repository.GigRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final ProfileService profileService;

    // Create new gig
    public GigDTO createGig(GigDTO gigDTO) {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        if (freelancerProfileRepository.findById(currentUser.getId()).isEmpty()) {
            throw new RuntimeException("Freelancer profile not found for user id: " + currentUser.getId());
        }

        GigEntity entity = toEntity(gigDTO, currentUser);

        entity = gigRepository.save(entity);

        return toDTO(entity);
    }

    // Get gig by id
    public GigDTO getGigById(Long gigId) {

        GigEntity entity = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("Gig not found with id: " + gigId));

        return toDTO(entity);
    }

    // Marketplace gigs (only active)
    public List<GigDTO> getAllGigs() {

        return gigRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Freelancer dashboard (active gigs)
    public List<GigDTO> getMyActiveGigs() {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        return gigRepository.findByFreelancerAndIsActiveTrue(currentUser)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Freelancer dashboard (all gigs including inactive)
    public List<GigDTO> getMyGigs() {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        return gigRepository.findByFreelancer(currentUser)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Soft delete gig
    public boolean deleteGig(Long gigId) {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        GigEntity gig = gigRepository
                .findByIdAndFreelancer(gigId, currentUser)
                .orElseThrow(() -> new RuntimeException("Gig not found or unauthorized"));

        gig.setIsActive(false);

        gigRepository.save(gig);

        return true;
    }

    // Restore gig
    public GigDTO restoreGig(Long gigId) {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        GigEntity gig = gigRepository
                .findByIdAndFreelancer(gigId, currentUser)
                .orElseThrow(() -> new RuntimeException("Gig not found or unauthorized"));

        gig.setIsActive(true);

        gig = gigRepository.save(gig);

        return toDTO(gig);
    }

    // Update gig
    public GigDTO updateGig(Long gigId, GigDTO gigDTO) {

        ProfileEntity currentUser = profileService.getCurrentProfile();

        GigEntity gig = gigRepository
                .findByIdAndFreelancer(gigId, currentUser)
                .orElseThrow(() -> new RuntimeException("Gig not found or unauthorized"));

        gig.setTitle(gigDTO.getTitle());
        gig.setDescription(gigDTO.getDescription());
        gig.setPrice(gigDTO.getPrice());
        gig.setDeliveryDays(gigDTO.getDeliveryDays());
        gig.setCategory(gigDTO.getCategory());

        gig = gigRepository.save(gig);

        return toDTO(gig);
    }

    // Convert DTO → Entity
    public GigEntity toEntity(GigDTO gigDTO, ProfileEntity currentUser) {

        return GigEntity.builder()
                .freelancer(currentUser)
                .title(gigDTO.getTitle())
                .description(gigDTO.getDescription())
                .price(gigDTO.getPrice())
                .deliveryDays(gigDTO.getDeliveryDays())
                .category(gigDTO.getCategory())
                .isActive(true)
                .build();
    }

    // Convert Entity → DTO
    public GigDTO toDTO(GigEntity gigEntity) {

        return GigDTO.builder()
                .id(gigEntity.getId())
                .title(gigEntity.getTitle())
                .description(gigEntity.getDescription())
                .price(gigEntity.getPrice())
                .deliveryDays(gigEntity.getDeliveryDays())
                .category(gigEntity.getCategory())
                .isActive(gigEntity.getIsActive())
                .freelancerName(gigEntity.getFreelancer().getFullName())
                .freelancerProfileImage(
                        gigEntity.getFreelancer().getProfileImageUrl())
                .build();
    }
}