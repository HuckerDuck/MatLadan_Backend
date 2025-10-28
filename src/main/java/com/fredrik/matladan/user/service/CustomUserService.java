package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;

public interface CustomUserService {
    //? Create a new User (with hashed password, save it and return a safe response)
    CustomUserResponseDTO createUser(CreateUserDTO createUserDTO);

    //? Find a User by username and return a safe response
    CustomUserResponseDTO findByUsername(String username);

    //? Update the email of a User
    CustomUserResponseDTO updateEmail(String username, String email);

    //? Disable a User
    void disableUser(String username);
}
