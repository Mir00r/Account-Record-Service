package com.kamkaiz.accountrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Data Transfer Objects for authentication-related operations.
 * This class contains nested classes that handle different aspects of authentication:
 * - Login requests
 * - Registration requests
 * - JWT token responses
 *
 * Each nested class includes appropriate validation constraints and
 * represents a specific authentication operation in the system.
 *
 * @see com.kamkaiz.accountrecordservice.security.JwtAuthenticationFilter
 * @see com.kamkaiz.accountrecordservice.controller.AuthController
 */
public class AuthDTO {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    /**
     * DTO for handling user login requests.
     * Contains credentials required for user authentication.
     */
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;
        
        @NotBlank(message = "Password is required")
        private String password;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    /**
     * DTO for handling new user registration requests.
     * Contains all required information for creating a new user account
     * with appropriate validation constraints.
     */
    public static class RegisterRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;
        
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        private String password;
        
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    /**
     * DTO for returning JWT authentication response.
     * Contains the JWT token and its type for client authentication.
     */
    public static class JwtResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
    }
}