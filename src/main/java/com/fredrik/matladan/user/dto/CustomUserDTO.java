package com.fredrik.matladan.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomUserDTO(
        @NotNull
        @Size(min = 1, max = 50, message = "The Username needs atleast 1 letter")
        String username,

        @NotNull
        @Size (min = 5, max = 100, message = "Password needs to be atleast 5 charecters")
        String password,

        @NotNull
        @Email (message = "Needs to be a valid email")
        String email
) {
}
