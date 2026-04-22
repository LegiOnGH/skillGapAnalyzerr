package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoleRequestDTO {

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name too long")
    private String roleName;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category name too long")
    private String category;

    @NotEmpty(message = "Skills cannot be empty")
    @Size(max = 15, message = "Maximum 15 skills allowed")
    private List<@NotBlank(message = "Skill cannot be blank") String> skills;
}
