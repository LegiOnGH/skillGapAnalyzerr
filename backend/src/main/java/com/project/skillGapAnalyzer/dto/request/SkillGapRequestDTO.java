package com.project.skillGapAnalyzer.dto.request;

import com.project.skillGapAnalyzer.enums.Experience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SkillGapRequestDTO {

    @Size(max = 15, message = "You can select up to 15 skills")
    private List<String> userSkills;

    @NotBlank(message = "Target role is required")
    @Size(max = 50, message = "Target role too long")
    private String targetRole;

    private Experience experienceLevel = Experience.BEGINNER;

    private boolean includeRepos = false;
}
