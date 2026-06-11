package com.fredrik.matladan.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email")
        String email,

        @NotBlank(message = "OTP code is required")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
        String otp,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(
                regexp = "^(?=.*[a-zåäö])(?=.*[A-ZÅÄÖ])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-zåäöÅÄÖ\\d@$!%*?&]{8,}$",
                message = "Password must contain uppercase, lowercase, number and special character"
        )
        String newPassword
) {}