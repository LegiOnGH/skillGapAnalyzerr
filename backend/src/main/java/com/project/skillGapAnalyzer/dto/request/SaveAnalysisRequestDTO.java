package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SaveAnalysisRequestDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotEmpty(message = "User skills cannot be empty")
    private List<@NotBlank(message = "Skill cannot be blank") String> userSkills;

    @NotBlank(message = "Target role is required")
    private String targetRole;

    @NotEmpty(message = "Missing skills cannot be empty")
    private List<@NotBlank(message = "Skill cannot be blank") String> missingSkills;
}
