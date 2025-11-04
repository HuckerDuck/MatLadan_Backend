package com.fredrik.matladan.user.controller;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.service.CustomUserService;
import jakarta.validation.Valid;
import org.hibernate.annotations.ConcreteProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class CustomUserController {
    private final CustomUserService userService;

    public CustomUserController(CustomUserService userService) {
        this.userService = userService;
    }

    //
    // Get all users
    //
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomUserResponseDTO>> getAllUsers(){
        List<CustomUserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //
    // Add a new user
    //
    @PostMapping(("/register"))
    public ResponseEntity<CustomUserResponseDTO> addAUser(
            @Valid @RequestBody CreateUserDTO createUserDTO
    ){
        CustomUserResponseDTO responseDTO = userService.createUser(createUserDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //
    // Get a User by username
    //
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<CustomUserResponseDTO> getUserByUsername(
            @PathVariable String username
    ){
        CustomUserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}
