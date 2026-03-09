package com.Prasad.Jobsy2.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Prasad.Jobsy2.dto.AuthDTO;
import com.Prasad.Jobsy2.dto.ProfileDTO;
import com.Prasad.Jobsy2.entity.ProfileEntity;
import com.Prasad.Jobsy2.entity.Role;
import com.Prasad.Jobsy2.repository.ProfileRepository;
import com.Prasad.Jobsy2.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    public String activationURL;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){

        ProfileEntity newProfile = toEntity(profileDTO);
        if(profileDTO.getActiveRole() != null){
            newProfile.setActiveRole(profileDTO.getActiveRole());
        }
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        //send activation email
        String activationLink = activationURL + "/api/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your Jobsy account";
        String body = "Click on the following link to activate your account " + activationLink;
        emailService.sendEmail(newProfile.getEmail(),subject,body);

        return toDTO(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        ProfileEntity entity = ProfileEntity.builder()
        .id(profileDTO.getId())
        .fullName(profileDTO.getFullName())
        .email(profileDTO.getEmail())
        .profileImageUrl(profileDTO.getProfileImageUrl())
        .createdAt(profileDTO.getCreatedAt())
        .updatedAt(profileDTO.getUpdatedAt())
        .activeRole(profileDTO.getActiveRole())
        .build();

        if(profileDTO.getPassword() != null){
            entity.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        }

        return entity;
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity){
        return ProfileDTO.builder()
        .id(profileEntity.getId())
        .fullName(profileEntity.getFullName())
        .email(profileEntity.getEmail())
        .profileImageUrl(profileEntity.getProfileImageUrl())
        .createdAt(profileEntity.getCreatedAt())
        .updatedAt(profileEntity.getUpdatedAt())
        .activeRole(profileEntity.getActiveRole())
        .build();
    }

    public boolean activateProfile(String activationToken){
        return profileRepository.findByActivationToken(activationToken)
        .map(profile -> {
            profile.setIsActive(true);
            profileRepository.save(profile);
            return true;
        }).orElse(false);
    }

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email)
        .map(ProfileEntity::getIsActive)
        .orElse(false);
    }

    public ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email : " + authentication.getName()));
    }

    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentUser = null;
        if(email == null){
            currentUser = getCurrentProfile();
        }
        else{
            currentUser = profileRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email : " + email));
        }

        return toDTO(currentUser);
    }

    public Role getActiveRole(){
        return getCurrentProfile().getActiveRole();
    }

    public ProfileDTO switchRole(Role newRole) {

        ProfileEntity profile = getCurrentProfile();

        profile.setActiveRole(newRole);

        profile = profileRepository.save(profile);

        return toDTO(profile);
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authDTO.getEmail(),
                    authDTO.getPassword()
                )
            );

            var userDetails = (UserDetails)
                    authentication.getPrincipal();

            if(!isAccountActive(authDTO.getEmail())){
                throw new RuntimeException("Account not activated");
            }

            String token = jwtUtil.generateToken(userDetails);

            return Map.of(
                "token", token,
                "user", getPublicProfile(authDTO.getEmail())
            );
        }
        catch(BadCredentialsException e){
            throw e;
        }
        catch(RuntimeException e){
            throw e;
        }
    }
}
