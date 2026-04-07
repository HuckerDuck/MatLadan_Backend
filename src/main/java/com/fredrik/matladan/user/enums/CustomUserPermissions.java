package com.fredrik.matladan.user.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomUserPermissions {
    READ("READ"),
    WRITE("WRITE"),
    DELETE("DELETE");

    private final String customUserPermission;
}
