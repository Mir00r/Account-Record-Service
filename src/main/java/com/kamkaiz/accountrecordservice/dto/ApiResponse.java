package com.kamkaiz.accountrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper for API responses.
 * Follows DRY principle by providing a standardized response format.
 * 
 * @param <T> The type of data being wrapped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    
    /**
     * Creates a success response with data.
     *
     * @param data The data to be wrapped
     * @param <T> The type of data
     * @return ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }
    
    /**
     * Creates a success response with data and message.
     *
     * @param data The data to be wrapped
     * @param message Success message
     * @param <T> The type of data
     * @return ApiResponse instance
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    
    /**
     * Creates an error response.
     *
     * @param message Error message
     * @param <T> The type of data
     * @return ApiResponse instance
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}