package com.fredrik.matladan.security.controller;
import com.fredrik.matladan.security.dto.ForgotPasswordRequest;
import com.fredrik.matladan.security.dto.LoginRequest;
import com.fredrik.matladan.security.dto.ResetPasswordRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;



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

    /**
     * Forgot password — sends OTP to email.
     * Always returns 200 regardless of whether email exists (security best practice).
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        String email = request.email().toLowerCase().trim();

        customUserRepository.findByEmail(email).ifPresent(verificationService::sendPasswordResetOtp
        );

        // Always return same message — don't reveal if email is registered
        return ResponseEntity.ok(Map.of(
                "message", "If an account exists with that email, a reset code has been sent."
        ));
    }

    /**
     * Reset password — verifies OTP and updates password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        String email = request.email().toLowerCase().trim();

        // Verify OTP is valid — throws exception if not
        verificationService.verifyPasswordResetOtp(email, request.otp());

        // Update password
        CustomUser user = customUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        customUserRepository.save(user);

        logger.info("Password reset successful for user {}", email);

        return ResponseEntity.ok(Map.of(
                "message", "Password updated successfully. You can now log in."
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
