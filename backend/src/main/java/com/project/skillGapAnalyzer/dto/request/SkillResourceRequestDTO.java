package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SkillResourceRequestDTO {

    @NotBlank(message = "Skill is required")
    @Size(max = 50, message = "Skill name too long")
    private String skill;

    @NotEmpty(message = "Resources cannot be empty")
    @Size(max = 15, message = "Maximum 15 resources allowed")
    private List<@NotBlank @Pattern(regexp = "https?://.*", message = "Invalid URL") String> resources;
}
