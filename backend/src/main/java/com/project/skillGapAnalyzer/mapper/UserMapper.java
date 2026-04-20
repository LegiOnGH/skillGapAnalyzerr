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
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // raw password, encode in service
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

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
    }
}
