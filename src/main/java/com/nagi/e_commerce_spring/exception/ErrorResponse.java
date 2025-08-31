package com.nagi.e_commerce_spring.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private List<String> details;
}
