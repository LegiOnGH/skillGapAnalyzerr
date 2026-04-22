package com.project.skillGapAnalyzer.dto.request;

import com.project.skillGapAnalyzer.enums.Experience;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RepoRequestDTO {

    @NotEmpty(message = "Skills list cannot be empty")
    @Size(max = 5, message = "You can select up to 5 skills")
    private List<@NotBlank(message = "Skill cannot be blank") String> skills;

    private Experience experienceLevel;
}
