package com.project.skillGapAnalyzer.dto.request;

import com.project.skillGapAnalyzer.enums.Experience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SkillGapRequestDTO{

    @NotEmpty(message = "Skills cannot be empty")
    private List<@NotBlank(message = "Skill cannot be blank") String> userSkills;

    @NotBlank(message = "Target role is required")
    private String targetRole;

    private Experience experienceLevel = Experience.BEGINNER;

    private boolean includeRepos = false;

}
