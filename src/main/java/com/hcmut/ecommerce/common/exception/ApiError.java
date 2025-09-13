package com.hcmut.ecommerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private boolean success;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private String path; // request path

    public static ApiError of(String message, String errorCode, String path) {
        return new ApiError(false, message, errorCode, LocalDateTime.now(), path);
    }

    
}
