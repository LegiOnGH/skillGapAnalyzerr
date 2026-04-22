package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.response.MessageResponseDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.enums.UserRole;
import com.project.skillGapAnalyzer.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);

    private final UserService userService;

    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserRole role) {
        logger.info("Fetching users | page: {}, size: {}, search: {}, role: {}",
                page, size, search, role);
        return ResponseEntity.ok(
                userService.getUsers(page, size, search, role)
        );
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable @NotBlank String userId) {
        logger.info("Fetching user by id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/users/{userId}/promote")
    public ResponseEntity<MessageResponseDTO> promoteUser(@PathVariable @NotBlank String userId) {
        logger.info("Super admin promoting user to ADMIN: {}", userId);
        userService.promoteUser(userId);
        return ResponseEntity.ok(
                new MessageResponseDTO("User promoted to ADMIN"));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable @NotBlank String userId) {
        logger.info("Super admin deleting user: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok(
                new MessageResponseDTO("User deleted successfully"));
    }
}