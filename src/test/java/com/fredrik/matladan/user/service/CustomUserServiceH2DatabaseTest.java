package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest


//? Since I already have one setup I will use a local one for the test
//? This will have the h2 database included
//? Also works as a failsafe to not let it enter my real database
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false",

        //? After adding JWT the test fails since it can't find the secret key
        //! Therefore we set them here, note that for security this is not the same as
        //? We use for development or production

        "jwt.secret=dGVzdFNlY3JldEtleUZvckpXVFRlc3RpbmdQdXJwb3Nlc09ubHkxMjM0NTY3ODkw",
        "jwt.expiration=3600000"

        //? After adding this the testt finished successfully
})
public class CustomUserServiceH2DatabaseTest {
    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private CustomUserRepository customUserRepository;

    private CreateUserDTO createUserDTO;

    //? Part 1 - Arrange and Setup
    //?
    //? Add a user that will be used for the test
    @BeforeEach
    public void setUp() {
        createUserDTO = new CreateUserDTO(
                "Test@1234",
                "test@example.com"
        );
    }

    @Test
    @DisplayName("Should create a User and return a ResponseDTO without the Password")
    public void createUserTest() {
        CustomUserResponseDTO result = customUserService.createUser(createUserDTO);
        assertNotNull(result);
        assertEquals("test@example.com", result.email());
    }

    @Test
    @DisplayName("Saved user in the database should have password hashed")
    void createdUserTestShouldReturnWithAPasswordThatIsHashed() {
        customUserService.createUser(createUserDTO);
        Optional<CustomUser> saved = customUserRepository.findByEmail("test@example.com");
        assertTrue(saved.isPresent());
        assertEquals("test@example.com", saved.get().getEmail());
        assertNotEquals("Test@1234", saved.get().getPasswordHash());
    }

    @Test
    @DisplayName("Duplicated email should throw exception")
    void createdDuplicatedEmailShouldThrowException() {
        customUserService.createUser(createUserDTO);
        CreateUserDTO duplicate = new CreateUserDTO(
                "Other@1234",
                "test@example.com"
        );
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customUserService.createUser(duplicate);
        });
        assertEquals("User with email test@example.com already exists in the database", exception.getMessage());
    }
}
