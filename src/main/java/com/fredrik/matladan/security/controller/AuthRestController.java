package com.fredrik.matladan.security.controller;
import com.fredrik.matladan.security.dto.LoginRequest;
import com.fredrik.matladan.security.jwt.JwtUtils;
import com.fredrik.matladan.user.dto.CreateUserDTO;
import com.fredrik.matladan.user.dto.CustomUserResponseDTO;
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



    @PostMapping(("/register"))
    public ResponseEntity<CustomUserResponseDTO> addAUser(
            @Valid @RequestBody CreateUserDTO createUserDTO
    ){
        CustomUserResponseDTO responseDTO = userService.createUser(createUserDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    /**
     * Login Endpoint for the user loggin in
     *
     * Accepts JSON {"username": "user
     *               "password": "password}
     *
     * @param loginRequest DTO which is containing username and password from
     *                     a request bod
     * @return User information and a JWT token for the user
     *         Token should be stored in SecureStore in Frontend (React Native with Expo)
     *         Token now expires after 1 hour
     **
     **/
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            //? Add the DTO for the login request
            //? We use the information in it to authenticate the user
            @RequestBody LoginRequest loginRequest
    ) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        logger.info("Authentication successful for user: {}", username);

        // Step 1: Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
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

        logger.info("Authentication successful for user", username);

        return ResponseEntity.ok(Map.of(
                "username", username,
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }
}
