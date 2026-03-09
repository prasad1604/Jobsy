package com.Prasad.Jobsy2.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Prasad.Jobsy2.entity.ProfileEntity;
import com.Prasad.Jobsy2.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService{
    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity existingProfile = profileRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email : " + email));

        return User.builder()
        .username(existingProfile.getEmail())
        .password(existingProfile.getPassword())
        .authorities("ROLE_" + existingProfile.getActiveRole().name())
        .build();
    }
}
