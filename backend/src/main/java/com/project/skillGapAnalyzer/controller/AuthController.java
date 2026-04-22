package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.LoginRequestDTO;
import com.project.skillGapAnalyzer.dto.request.SignupRequestDTO;
import com.project.skillGapAnalyzer.dto.response.LoginResponseDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Valid @RequestBody SignupRequestDTO dto) {
        logger.info("User signup requested: {}", dto.getUserName());
        UserResponseDTO response = authService.signup(dto);
        logger.info("User signup successful: {} (id: {})", dto.getUserName(), response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        logger.debug("Login attempt for user: {}", dto.getUserName());
        LoginResponseDTO response = authService.login(dto);
        logger.info("User login successful: {}", dto.getUserName());
        return ResponseEntity.ok(response);
    }
}
