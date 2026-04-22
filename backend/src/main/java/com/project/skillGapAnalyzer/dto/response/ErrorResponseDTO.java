package com.project.skillGapAnalyzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private String message;
    private HttpStatus status;
    private Instant timestamp;
}
