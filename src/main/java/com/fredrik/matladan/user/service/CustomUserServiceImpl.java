package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.mapper.CustomUserMapper;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserServiceImpl implements CustomUserService{
    private final CustomUserRepository repository;
    private final CustomUserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public CustomUserServiceImpl(CustomUserRepository repository, CustomUserMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    //?
    //? Create a User
    //?

    @Override
    //? This is used in case there are problems during the creating method
    //? If there is an error then Spring will roll back the changes
    //? Nothing is then added to the database
    @Transactional
    public CustomUserResponseDTO createUser (CreateUserDTO createUserDTO){
        //? Trim is nice and is used to remove spaces from the username
        //? Makes " Fredrik  " into "Fredrik"
        //? Also makes sure that the username is unique
        String trimmedUsername = createUserDTO.username().trim();
        String toLowerCaseEmail = createUserDTO.email().toLowerCase();

        //? Make a check if the Username already exists
        if (repository.findByUsername(trimmedUsername).isPresent()){
            throw new RuntimeException("Username already exists in the database");
        }

        //? Make a check if the email already exist
        if(repository.findByEmail(toLowerCaseEmail).isPresent()){
            throw new RuntimeException("Email already exists in the database");
        }

        //? Mapper DTO -> Entity
        CustomUser customUser = mapper.toEntity(createUserDTO);

        customUser.setUsername(trimmedUsername);
        customUser.setEmail(toLowerCaseEmail);
        customUser.setPasswordHash(passwordEncoder.encode(createUserDTO.password()));

        CustomUser savedUser = repository.save(customUser);

        //? Mapper Entity -> Response DTO
        //? This is used to return a safe response to the client
        //? The password is not returned to the client
        return mapper.toResponseDTO(savedUser);
    }

    @Override
    public CustomUserResponseDTO findByUsername(String username) {
        return null;
    }

    @Override
    public CustomUserResponseDTO updateEmail(String username, String email) {
        return null;
    }

    @Override
    public void disableUser(String username) {

    }


}
