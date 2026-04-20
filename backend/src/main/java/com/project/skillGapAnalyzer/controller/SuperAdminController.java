package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class);

    private final UserService userService;

    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    // 🔹 Promote User to Admin
    @PutMapping("/promote/{userId}")
    public ResponseEntity<String> promoteUser(@PathVariable String userId) {

        logger.info("Promoting user to ADMIN: {}", userId);
        userService.promoteUser(userId);

        return ResponseEntity.ok("User promoted to ADMIN");
    }

    // 🔹 Delete User
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {

        logger.info("Deleting user: {}", userId);
        userService.deleteUser(userId);

        return ResponseEntity.ok("User deleted successfully");
    }
}