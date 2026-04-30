package com.project.skillGapAnalyzer.service;

import com.project.skillGapAnalyzer.dto.request.UpdateProfileRequestDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.exceptions.BadRequestException;
import com.project.skillGapAnalyzer.exceptions.ResourceNotFoundException;
import com.project.skillGapAnalyzer.mapper.UserMapper;
import com.project.skillGapAnalyzer.model.User;
import com.project.skillGapAnalyzer.repository.UserRepository;
import com.project.skillGapAnalyzer.util.StringNormalizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProfileService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUserNameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponseDTO getMe() {
        return userMapper.toDTO(getCurrentUser());
    }

    public UserResponseDTO updateMe(UpdateProfileRequestDTO dto) {
        User user = getCurrentUser();

        if (dto.getUserName() != null && !dto.getUserName().isBlank()) {
            String normalized = StringNormalizer.normalize(dto.getUserName());
            if (userRepository.existsByUserNameIgnoreCase(normalized) &&
                    !normalized.equals(StringNormalizer.normalize(user.getUserName()))) {
                throw new BadRequestException("Username already taken");
            }
            user.setUserName(dto.getUserName().trim());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String normalized = StringNormalizer.normalize(dto.getEmail());
            if (userRepository.existsByEmailIgnoreCase(normalized) &&
                    !normalized.equals(StringNormalizer.normalize(user.getEmail()))) {
                throw new BadRequestException("Email already in use");
            }
            user.setEmail(dto.getEmail().trim().toLowerCase());
        }

        userRepository.save(user);
        return userMapper.toDTO(user);
    }
}