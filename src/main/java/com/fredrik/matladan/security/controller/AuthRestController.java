package com.fredrik.matladan.security.controller;
import com.fredrik.matladan.security.dto.LoginRequest;
import com.fredrik.matladan.security.dto.VerifyOTPRequest;
import com.fredrik.matladan.security.jwt.JwtUtils;
import com.fredrik.matladan.security.service.VerificationService;
import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
import com.fredrik.matladan.user.exceptions.UserNotFoundException;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import com.fredrik.matladan.user.service.CustomUserService;
import com.fredrik.matladan.user.userdetails.CustomUserDetailsImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@AllArgsConstructor
@RestController()
@RequestMapping("/api/auth")
public class AuthRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserService userService;
    private final VerificationService verificationService;
    private final CustomUserRepository customUserRepository;



    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> addAUser(
            @Valid @RequestBody CreateUserDTO createUserDTO
    ) {

        CustomUserResponseDTO responseDTO = userService.createUser(createUserDTO);


        CustomUser savedUser = customUserRepository.findByEmail(createUserDTO.email().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException(createUserDTO.email()));


        verificationService.sendVerificationOtp(savedUser);


        return ResponseEntity.status(201).body(Map.of(
                "message", "Account created. Please check your email for a verification code.",
                "email", responseDTO.email()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            //? Add the DTO for the login request
            //? We use the information in it to authenticate the user
            @RequestBody LoginRequest loginRequest
    ) {
        String email = loginRequest.getEmail().toLowerCase().trim();
        String password = loginRequest.getPassword();

        // Step 1: Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetailsImpl userDetails) {
            logger.debug("User Details: {}, Authorities {}, Enabled: {}",
                    userDetails.getUsername()
                    , userDetails.getAuthorities()
                    , userDetails.isEnabled());
        }



        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();


        String token = jwtUtils.generateJwtToken(customUserDetails.getUser());

        logger.info("Authentication successful for user", email);

        return ResponseEntity.ok(Map.of(
                "email", email,
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyEmail(
            @Valid @RequestBody VerifyOTPRequest request
    ) {
        verificationService.verifyEmail(request.email(), request.otp());
        return ResponseEntity.ok(Map.of("message", "Email verified successfully. You can now log in."));
    }

}
