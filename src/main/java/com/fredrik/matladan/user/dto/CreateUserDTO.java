package com.fredrik.matladan.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record CreateUserDTO(
        @NotBlank
        @Size(min = 1, max = 50, message = "The Username needs atleast 1 letter")
        String username,

        @NotBlank
        @Size (min = 5, max = 100, message = "Password needs to be atleast 5 charecters")
        String password,

        @NotBlank
        @Email (message = "Needs to be a valid email")
        String email
) {
}
