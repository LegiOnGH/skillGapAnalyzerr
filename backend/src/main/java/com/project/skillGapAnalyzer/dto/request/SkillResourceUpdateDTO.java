package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SkillResourceUpdateDTO {

    @NotBlank(message = "Old resource cannot be blank")
    @Size(max = 500, message = "Resource too long")
    @Pattern(regexp = "https?://.*", message = "Invalid resource URL")
    private String oldResource;

    @NotBlank(message = "New resource cannot be blank")
    @Size(max = 500, message = "Resource too long")
    @Pattern(regexp = "https?://.*", message = "Invalid resource URL")
    private String newResource;
}
