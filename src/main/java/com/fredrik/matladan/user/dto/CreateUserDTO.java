package com.fredrik.matladan.user.dto;

import jakarta.validation.constraints.*;

public record CreateUserDTO(
        @NotBlank
        @Size (min = 5, max = 100, message = "Password needs to be atleast 5 charecters")
        @Pattern(
                regexp = "^(?=.*[a-zåäö])(?=.*[A-ZÅÄÖ])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-zåäöÅÄÖ\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character (@$!%*?&)"
        )
        String password,

        @NotBlank
        @Email (message = "Needs to be a valid email")
        String email
) {
}
