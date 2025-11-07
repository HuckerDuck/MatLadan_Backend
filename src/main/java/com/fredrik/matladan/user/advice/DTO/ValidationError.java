package com.fredrik.matladan.user.advice.DTO;

public record ValidationError (
        String field,
        String message
) {
}
