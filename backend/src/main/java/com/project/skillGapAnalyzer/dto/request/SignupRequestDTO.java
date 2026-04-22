package com.project.skillGapAnalyzer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {

    @NotBlank(message = "Username required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^\\S+$", message = "Username must not contain spaces")
    private String userName;

    @NotBlank(message = "Email required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
            message = "Password must include uppercase, lowercase, number and special character"
    )
    private String password;
}
