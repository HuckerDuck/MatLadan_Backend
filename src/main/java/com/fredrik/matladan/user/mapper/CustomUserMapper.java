package com.fredrik.matladan.user.mapper;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.stereotype.Component;

@Component
public class CustomUserMapper {
    //? Till entit

    //TODO Add a MapStruct annotation to this method

    //! Small change here,
    //! Was returning the password which I don't wanna do
    //! So I changed that
    public CustomUser toEntity(CreateUserDTO createUserDTO) {
        CustomUser customUser = new CustomUser();
        customUser.setUsername(createUserDTO.username());
        customUser.setEmail(createUserDTO.email());

        return customUser;
    }

    public CustomUserResponseDTO toResponseDTO(CustomUser customUser) {
        return new CustomUserResponseDTO(
                customUser.getUsername(),
                customUser.getEmail()
        );
    }
}
