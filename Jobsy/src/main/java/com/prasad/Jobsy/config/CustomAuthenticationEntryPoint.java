package com.prasad.Jobsy.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Create a more detailed error response
        ErrorResponse errorResponse = ErrorResponse.builder()
            .authenticated(false)
            .message("Authentication required to access this resource")
            .error("Unauthorized")
            .status(401)
            .path(request.getRequestURI())
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // Inner class for error response structure using Lombok
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ErrorResponse {
        private boolean authenticated;
        private String message;
        private String error;
        private int status;
        private String path;
        private String timestamp;
    }
}
