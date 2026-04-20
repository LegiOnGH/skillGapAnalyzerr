package com.project.skillGapAnalyzer.dto.response;

import com.project.skillGapAnalyzer.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;

    private UserRole role;
}
