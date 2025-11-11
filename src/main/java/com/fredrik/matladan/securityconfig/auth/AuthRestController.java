package com.fredrik.matladan.securityconfig.auth;
import com.fredrik.matladan.securityconfig.jwt.JwtUtils;
import com.fredrik.matladan.user.userdetails.CustomUserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class AuthRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        logger.debug("Attempting authentication for user: {}", username);

        // Step 1: Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        System.out.println("\n========= AUTHENTICATION RESULT =========");
        System.out.println("Class: " + authentication.getClass().getSimpleName());
        System.out.println("Authenticated: " + authentication.isAuthenticated());

        Object principal = authentication.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getSimpleName());
        if (principal instanceof CustomUserDetailsImpl userDetails) {
            System.out.println("  Username: " + userDetails.getUsername());
            System.out.println("  Authorities: " + userDetails.getAuthorities());
            System.out.println("  Account Non Locked: " + userDetails.isAccountNonLocked());
            System.out.println("  Account Enabled: " + userDetails.isEnabled());
            System.out.println("  Password (hashed): " + userDetails.getPassword());
        } else {
            System.out.println("Principal value: " + principal);
        }

        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("=========================================\n");


        CustomUserDetailsImpl customUserDetails = (CustomUserDetailsImpl) authentication.getPrincipal();


        String token = jwtUtils.generateJwtToken(customUserDetails.getUser());

        // Set cookie
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        logger.info("Authentication successful for user: {}", username);

        return ResponseEntity.ok(Map.of(
                "username", username,
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }
}
