package com.fredrik.matladan.security.refreshtoken.service;

import com.fredrik.matladan.security.refreshtoken.RefreshToken;
import com.fredrik.matladan.security.refreshtoken.repository.RefreshTokenRepository;
import com.fredrik.matladan.user.model.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs;

    @Transactional
    public RefreshToken createRefreshToken(CustomUser user) {
        // Delete any existing refresh token for this user before creating new one
        refreshTokenRepository.deleteAllByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Refresh token has expired. Please log in again.");
        }

        return refreshToken;
    }

    // Token rotation — delete old token after use, caller creates a new one
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public void deleteAllForUser(CustomUser user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}