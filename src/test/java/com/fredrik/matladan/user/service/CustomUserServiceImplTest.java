package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.mapper.CustomUserMapper;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//? This one is importan since it instances Mockito
@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {
    @Mock
    CustomUserRepository customUserRepository;
    @Mock
    CustomUserMapper customUserMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    CustomUserServiceImpl customUserServiceImpl;

    private CreateUserDTO createUserDTO;
    private CustomUser customUser;
    private CustomUserResponseDTO customUserResponseDTO;


    //?
    //? Test 1 - Se if I can create a user
    //?

    @Test
    void createUser_ShouldReturnCorretly(){
         //? Arrange and prepare for
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "password",
                "password"
        );


        CustomUser customUser = new CustomUser();
        customUser.setEmail("testEmail");
        customUser.setPasswordHash("passwordhash");

        CustomUserResponseDTO customUserResponseDTO = new CustomUserResponseDTO(
                "testUsername"
        );

        when(customUserMapper.toEntity(createUserDTO)).thenReturn(customUser);
        when(customUserRepository.save(customUser)).thenReturn(customUser);
        when(customUserMapper.toResponseDTO(customUser)).thenReturn(customUserResponseDTO);

        CustomUserResponseDTO result = customUserServiceImpl.createUser(createUserDTO);



        assertEquals("testUsername", result.email());

        System.out.println();
        System.out.println("Expect that the response from the ResponseDTO");
        System.out.println("Matches when you created the user");
        System.out.println();


    }







}