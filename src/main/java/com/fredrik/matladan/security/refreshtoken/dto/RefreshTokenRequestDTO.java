package com.fredrik.matladan.security.refreshtoken.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO (
        @NotBlank(message = "Refresh token is required")
        String refreshToken
){ }
