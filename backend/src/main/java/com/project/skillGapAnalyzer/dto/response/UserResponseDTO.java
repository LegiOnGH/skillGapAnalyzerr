package com.project.skillGapAnalyzer.dto.response;

import com.project.skillGapAnalyzer.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String id;
    private String userName;
    private String email;
    private UserRole role;
}
