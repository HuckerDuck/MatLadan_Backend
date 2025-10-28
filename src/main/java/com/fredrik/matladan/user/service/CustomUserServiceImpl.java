package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.mapper.CustomUserMapper;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
    //? If there is an error, then Spring will roll back the changes
    //? Nothing is then added to the database
    @Transactional
    public CustomUserResponseDTO createUser (CreateUserDTO createUserDTO){
        //? Trim is nice and is used to remove spaces from the username
        //? Makes " Fredrik " into "Fredrik"
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

    //?
    //? Find a User by username
    //?

    @Override
    @Transactional
    public CustomUserResponseDTO findByUsername(String username) {
        String trimmedUsername = username.trim();

        Optional<CustomUser> user = repository.findByUsername(trimmedUsername);

        //? Check if the user exists or not
        if (user.isEmpty()){
            throw new RuntimeException("User does not exist");
        }

        return mapper.toResponseDTO(user.get());
     }


    //?
    //? Update the email of a User
    //?

    @Override
    @Transactional
    public CustomUserResponseDTO updateEmail(String username, String email) {
        String trimmedUsername = username.trim();
        String toLowerCaseEmail = email.toLowerCase();

        Optional<CustomUser> user = repository.findByUsername(trimmedUsername);

        //? Check if the user exists or not
        if (user.isEmpty()){
            throw new RuntimeException("User does not exist");
        }

        //? Make a check if the email already exist
        if(repository.findByEmail(toLowerCaseEmail).isPresent()){
            throw new RuntimeException("Email already exists in the database");
        }

        CustomUser updatedUser = user.get();
        updatedUser.setEmail(toLowerCaseEmail);

        CustomUser savedUser = repository.save(updatedUser);

        return mapper.toResponseDTO(savedUser);
    }

    //?
    //? Disable a User
    //?

    @Override
    @Transactional
    public void disableUser(String username) {
        String trimmedUsername = username.trim();

        Optional<CustomUser> customUser = repository.findByUsername(trimmedUsername);

        //? Check if the user exists or not
        if (customUser.isEmpty()){
            throw new RuntimeException("User does not exist");
        }

        CustomUser user = customUser.get();

        //? Changes the boolean of the user to false
        //? Therefore this mean that the user is disabled
        user.setEnabled(false);

        repository.save(user);
    }


}
