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
        "spring.flyway.enabled=false"
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
                "BennyTest",
                "Password123",
                //? Not that this will still be set to benny@example.test because of the
                //? .ToLowerCase in the Serviceimpl
                "BeNny@example.test"
        );
    }

    @AfterEach
    void tearDown() {
        customUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a User and Password and return a ResponseDTO without the Password")
    public void createUserTest(){
        //? Part 2 - Act
        CustomUserResponseDTO responseDTORetults = customUserService.createUser(createUserDTO);

        //? Part 3 - Assert

        //Expect this not to be empty
        assertNotNull(responseDTORetults);

        // Expect the username to be the same as in the Setup above
        assertEquals("BennyTest", responseDTORetults.username());

        // Expect the email to be the as in the Setup above
        assertEquals("benny@example.test", responseDTORetults.email());
    }

    @Test
    @DisplayName("Saved user in the database should have password hashed")
    void createdUserTestShouldReturnWithAPasswordThatIsHashed(){
        //? Step 2 - Act
        CustomUserResponseDTO responseDTORetults = customUserService.createUser(createUserDTO);

        //? Step 3 - Assert
        //? We use the repository to find the user we have added above in the H2 database
        Optional<CustomUser> customUserSaved = customUserRepository.findByUsername("BennyTest");

        //? We expect the user to be saved in the database
        assertTrue(customUserSaved.isPresent());

        assertEquals("BennyTest", customUserSaved.get().getUsername());

        assertEquals("benny@example.test", customUserSaved.get().getEmail());

        //? We expect that the password isn't the same anymore since it should be hashed
        //? If we set this to true then the test will fail since we won't get
        //? Password123 but the long hash instead
        assertNotEquals("Password123", customUserSaved.get().getPasswordHash());
    }

    @Test
    @DisplayName("Duplicated user inserted, this should throw a exception")

    void createdDuplicatedCustomUserShouldThrowException(){
        //? Step 1 - Arrange
        customUserService.createUser(createUserDTO);

        //? We had a new user, note that 1 in already added since we before
        CreateUserDTO createUserDuplicate = new CreateUserDTO(
                "BennyTest",
                "abadpassword123",
                "BennyTestTheSecond@Example.kristoffer"
        );

        //? Step 2 and 3 - Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customUserService.createUser(createUserDuplicate);
        });

        assertEquals("Username already exists in the database", exception.getMessage());

    }
}
