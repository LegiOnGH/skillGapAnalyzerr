package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^\\S+$", message = "Username must not contain spaces")
    private String userName;

    @Email(message = "Enter a valid email address")
    private String email;
}
