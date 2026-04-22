package com.project.skillGapAnalyzer.dto.response;

import com.project.skillGapAnalyzer.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String accessToken;
    private String tokenType;
    private UserRole role;
    private String userId;
}
