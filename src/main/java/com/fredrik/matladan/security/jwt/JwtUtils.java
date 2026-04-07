package com.fredrik.matladan.security.jwt;

import com.fredrik.matladan.user.enums.CustomUserRole;
import com.fredrik.matladan.user.model.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String base64secretKey;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationInMs;

    private SecretKey getTheKeyForSigningIn(){
        byte[] keyBytes = Base64.getDecoder().decode(base64secretKey);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 256 bits after the Base64 decode.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(CustomUser user){

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claim("authorities", List.of("ROLE_" + user.getRole().name()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getTheKeyForSigningIn())
                .compact();

        return token;
    }

    public String getTheUserNameFromJwtToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getTheKeyForSigningIn())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();

            return username;
        }

        catch (Exception e){
            logger.error("Error while getting the username from the JWT token", e.getMessage());
            return null;
        }
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parser()
                    .verifyWith(getTheKeyForSigningIn())
                    .build()
                    .parseSignedClaims(authToken);

            logger.debug("JWT token is valid");
            return true;
        }
        catch (Exception e){
            logger.error("JWT token is invalid", e.getMessage());

        }
        return false;
    }

    public Set<CustomUserRole> getRolesFromJwtToken(String token){
        Claims claims  = Jwts.parser()
                .verifyWith(getTheKeyForSigningIn())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> authoritiesClaim = claims.get("authorities", List.class);

        if(authoritiesClaim == null || authoritiesClaim.isEmpty()){
            logger.warn("No roles found in the JWT token");
            return Set.of();
        }

        //? Convert each string like "ROLE_User" -> UserRole.USER
        Set<CustomUserRole > roles = authoritiesClaim. stream()
                .filter(String.class::isInstance )
                .map(String.class::cast)
                .map(role -> role.replace("ROLE_", ""))
                .map(String::toUpperCase )
                .map(CustomUserRole ::valueOf) // map to my enum
                .collect(Collectors.toSet());
        logger.debug("Extracted roles from JWT token: {}" , roles);
        return roles;
    }
}
