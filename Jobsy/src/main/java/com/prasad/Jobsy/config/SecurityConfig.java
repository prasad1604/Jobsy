package com.prasad.Jobsy.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.prasad.Jobsy.filter.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable method-level security
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers(
                    "/login", 
                    "/register", 
                    "/send-reset-otp", 
                    "/reset-password", 
                    "/logout",
                    "/api/roles/all", // Allow viewing available roles
                    "/api/jobs", // Allow browsing jobs without auth
                    "/api/jobs/{id}",
                    "/api/jobs/job-id/{jobId}",
                    "/api/jobs/status/{status}",
                    "/api/jobs/search",
                    "/api/jobs/category/{category}"
                ).permitAll()
                
                // Role management endpoints (authenticated users only)
                .requestMatchers("/api/roles/**").authenticated()
                
                // Job management endpoints
                .requestMatchers("/api/jobs/my-jobs").authenticated()
                .requestMatchers("/api/jobs/{id}").authenticated()
                .requestMatchers("/api/jobs/{id}/status").authenticated()
                
                // Proposal endpoints (authenticated users only)
                .requestMatchers("/api/proposals/**").authenticated()
                
                // Message endpoints (authenticated users only)
                .requestMatchers("/api/messages/**").authenticated()
                
                // Contract endpoints (authenticated users only)
                .requestMatchers("/api/contracts/**").authenticated()
                
                // Payment endpoints (authenticated users only)
                .requestMatchers("/api/payments/**").authenticated()
                
                // Profile endpoints (authenticated users only)
                .requestMatchers("/profile/**").authenticated()
                
                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:5173", 
            "http://localhost:3000", 
            "http://localhost:8080"
        )); // Add more origins as needed
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
