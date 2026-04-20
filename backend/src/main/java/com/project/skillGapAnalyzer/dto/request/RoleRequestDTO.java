package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RoleRequestDTO {

    @NotBlank(message = "Role name is required")
    private String roleName;

    @NotBlank(message = "Category is required")
    private String category;

    @NotEmpty(message = "Skills cannot be empty")
    private List<@NotBlank(message = "Skill cannot be blank") String> skills;
}
