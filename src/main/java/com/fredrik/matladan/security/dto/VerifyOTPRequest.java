package com.fredrik.matladan.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOTPRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email")
        String email,

        @NotBlank(message = "OTP code is required")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
        String otp
) {}
