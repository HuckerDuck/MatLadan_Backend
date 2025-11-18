package com.fredrik.matladan.user.controller;

import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.service.CustomUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class CustomUserController {
    private final CustomUserService userService;

    //
    // Get all users
    //
    @GetMapping("/getallusers")
    public ResponseEntity<List<CustomUserResponseDTO>> getAllUsers(){
        List<CustomUserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //
    // Get a User by username
    //
    @GetMapping("/{username}")
    public ResponseEntity<CustomUserResponseDTO> getUserByUsername(
            @PathVariable String username
    ){
        CustomUserResponseDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    //! Missing a disable user endpoint
}
