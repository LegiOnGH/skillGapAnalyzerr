package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SkillResourceRequestDTO {

    @NotBlank(message = "Skill is required")
    private String skill;

    @NotEmpty(message = "Resources cannot be empty")
    private List<@NotBlank(message = "Resource cannot be blank") String> resources;
}
