package com.project.skillGapAnalyzer.controller;

import com.project.skillGapAnalyzer.dto.request.UpdateProfileRequestDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ProfileService profileService;

    public UserController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe() {
        return ResponseEntity.ok(profileService.getMe());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMe(
            @Valid @RequestBody UpdateProfileRequestDTO dto) {
        return ResponseEntity.ok(profileService.updateMe(dto));
    }
}