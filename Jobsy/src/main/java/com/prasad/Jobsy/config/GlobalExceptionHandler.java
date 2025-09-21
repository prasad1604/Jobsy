package com.prasad.Jobsy.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = createErrorResponse(
            "Validation failed", 
            HttpStatus.BAD_REQUEST.value(), 
            errors
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        Map<String, Object> response = createErrorResponse(
            ex.getMessage(), 
            HttpStatus.BAD_REQUEST.value(), 
            null
        );
        
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex) {
        
        Map<String, Object> response = createErrorResponse(
            ex.getReason(), 
            ex.getStatusCode().value(), 
            null
        );
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(
            UsernameNotFoundException ex) {
        
        Map<String, Object> response = createErrorResponse(
            "User not found", 
            HttpStatus.NOT_FOUND.value(), 
            null
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(
            BadCredentialsException ex) {
        
        Map<String, Object> response = createErrorResponse(
            "Invalid credentials", 
            HttpStatus.UNAUTHORIZED.value(), 
            null
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex) {
        
        Map<String, Object> response = createErrorResponse(
            "Access denied", 
            HttpStatus.FORBIDDEN.value(), 
            null
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        
        Map<String, Object> response = createErrorResponse(
            "An unexpected error occurred", 
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            null
        );
        
        // Log the actual exception for debugging
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private Map<String, Object> createErrorResponse(String message, int status, Object details) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        if (details != null) {
            response.put("errors", details);
        }
        
        return response;
    }
}
