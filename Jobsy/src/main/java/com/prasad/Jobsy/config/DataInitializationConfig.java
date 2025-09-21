package com.prasad.Jobsy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.prasad.Jobsy.service.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializationConfig {

    private final RoleService roleService;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            try {
                // Initialize default roles
                initializeRoles();
                log.info("Data initialization completed successfully");
            } catch (Exception e) {
                log.error("Error during data initialization", e);
            }
        };
    }

    private void initializeRoles() {
        try {
            // Create FREELANCER role if it doesn't exist
            try {
                roleService.findByRoleName("FREELANCER");
            } catch (Exception e) {
                roleService.createRole("FREELANCER", "Users who provide freelance services");
                log.info("Created FREELANCER role");
            }

            // Create HIRER role if it doesn't exist
            try {
                roleService.findByRoleName("HIRER");
            } catch (Exception e) {
                roleService.createRole("HIRER", "Users who hire freelancers for projects");
                log.info("Created HIRER role");
            }

        } catch (Exception e) {
            log.error("Error initializing roles", e);
        }
    }
}
