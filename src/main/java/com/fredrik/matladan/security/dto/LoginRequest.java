package com.fredrik.matladan.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//? This is the request body for the login endpoint
//? This will be used later when we send this from FrontEnd
public class LoginRequest {
    private String username;
    private String password;
}
