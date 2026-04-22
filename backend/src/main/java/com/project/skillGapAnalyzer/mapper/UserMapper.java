package com.project.skillGapAnalyzer.mapper;

import com.project.skillGapAnalyzer.dto.request.SignupRequestDTO;
import com.project.skillGapAnalyzer.dto.response.UserResponseDTO;
import com.project.skillGapAnalyzer.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(SignupRequestDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .userName(dto.getUserName().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .build();
    }

    public UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public void updateUserFromDTO(SignupRequestDTO dto, User user) {
        if (dto == null || user == null) return;

        if (dto.getUserName() != null) {
            user.setUserName(dto.getUserName().trim());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail().trim().toLowerCase());
        }
    }
}
