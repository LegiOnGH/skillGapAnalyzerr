package com.project.skillGapAnalyzer.dto.request;

import com.project.skillGapAnalyzer.enums.Experience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RepoRequestDTO {

    @NotEmpty(message = "Skills list cannot be empty")
    private List<@NotBlank(message = "Skill cannot be blank") String> skills;

    @NotNull(message = "Experience level is required")
    private Experience experienceLevel;
}
