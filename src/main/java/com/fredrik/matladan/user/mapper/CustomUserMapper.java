package com.fredrik.matladan.user.mapper;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.stereotype.Component;

@Component
public class CustomUserMapper {
    //? Till entit
    public CustomUser toEntity(CreateUserDTO createUserDTO) {
        return new CustomUser(
                createUserDTO.username(),
                createUserDTO.password(),
                createUserDTO.email()
        );
    }

    public CustomUserResponseDTO toResponseDTO(CustomUser customUser) {
        return new CustomUserResponseDTO(
                customUser.getUsername(),
                customUser.getEmail()
        );
    }
}
