package com.project.skillGapAnalyzer.dto.request;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^\\S+$", message = "Username must not contain spaces")
    @NotBlank(message = "Username required")
    private String userName;

    @NotBlank(message = "Email required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Password required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number and one special character.")
    private String password;

}
