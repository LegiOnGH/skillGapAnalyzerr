package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateDTO {

    @Size(max = 50, message = "Role name too long")
    private String roleName;

    @Size(max = 50, message = "Category too long")
    private String category;

    @Size(max = 20, message = "Maximum 20 skills allowed")
    private List<@NotBlank(message = "Skill cannot be blank") String> skills;

}
