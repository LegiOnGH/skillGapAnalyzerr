package com.project.skillGapAnalyzer.dto.request;

import com.project.skillGapAnalyzer.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleUpdateRequestDTO {

    @NotNull(message = "Role is required")
    private UserRole role;
}
