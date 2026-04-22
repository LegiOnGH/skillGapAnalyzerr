package com.project.skillGapAnalyzer.exceptions;

import com.project.skillGapAnalyzer.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequest(BadRequestException ex) {

        logger.warn("Bad request", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        Instant.now()
                ));
    }

    //404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {

        logger.warn("Resource not found", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND,
                        Instant.now()
                ));
    }

    //401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {

        logger.warn("Authentication failed");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(
                        "Invalid username or password",
                        HttpStatus.UNAUTHORIZED,
                        Instant.now()
                ));
    }

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        logger.warn("Validation failed: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        "Validation failed: " + errors,
                        HttpStatus.BAD_REQUEST,
                        Instant.now()
                ));
    }

    //403
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {

        logger.warn("Access denied: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDTO(
                        "You do not have permission to perform this action",
                        HttpStatus.FORBIDDEN,
                        Instant.now()
                ));
    }

    //502
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponseDTO> handleExternal(ExternalServiceException ex) {

        logger.error("External service error", ex);

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponseDTO(
                        "External service failed",
                        HttpStatus.BAD_GATEWAY,
                        Instant.now()
                ));
    }

    //500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {

        logger.error("Unexpected error occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        "Something went wrong",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        Instant.now()
                ));
    }
}
