package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name too long")
    private String name;
}
