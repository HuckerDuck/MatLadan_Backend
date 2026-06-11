package com.fredrik.matladan.security.service;

import com.fredrik.matladan.security.VerificationEntity.VerificationEntity;
import com.fredrik.matladan.security.VerificationEntity.VerificationTokenType;
import com.fredrik.matladan.security.repository.VerificationRepository;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class VerificationServiceImpl implements VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);
    private final VerificationRepository verificationRepository;
    private final CustomUserRepository customUserRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public void sendVerificationOtp(CustomUser user) {
        String otp = generateOtp();
        saveOtp(user, otp, VerificationTokenType.VERIFICATION);
        emailService.sendVerificationOtp(user.getEmail(), otp);
        logger.info("Verification OTP sent to {}", user.getEmail());
    }

    @Override
    @Transactional
    public void sendPasswordResetOtp(CustomUser user) {
        String otp = generateOtp();
        saveOtp(user, otp, VerificationTokenType.RESET);
        emailService.sendPasswordResetOtp(user.getEmail(), otp);
        logger.info("Password reset OTP sent to {}", user.getEmail());
    }

    @Override
    @Transactional
    public void verifyEmail(String email, String otp) {
        String normalizedEmail = email.toLowerCase().trim();

        CustomUser user = customUserRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification code"));

        VerificationEntity token = verificationRepository
                .findByTokenAndTokenType(otp, VerificationTokenType.VERIFICATION)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification code"));

        if (!token.getTokenOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        if (token.isExpired()) {
            verificationRepository.delete(token);
            throw new IllegalArgumentException("Verification code has expired. Please request a new one.");
        }

        user.setEnabled(true);
        customUserRepository.save(user);
        verificationRepository.delete(token);
        logger.info("Email verified for user {}", normalizedEmail);
    }

    @Override
    @Transactional
    public void verifyPasswordResetOtp(String email, String otp) {
        String normalizedEmail = email.toLowerCase().trim();

        CustomUser user = customUserRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset code"));

        VerificationEntity token = verificationRepository
                .findByTokenAndTokenType(otp, VerificationTokenType.RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset code"));

        if (!token.getTokenOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Invalid reset code");
        }
        if (token.isExpired()) {
            verificationRepository.delete(token);
            throw new IllegalArgumentException("Reset code has expired. Please request a new one.");
        }

        // Token is valid — delete it so it can't be reused
        verificationRepository.delete(token);
        logger.info("Password reset OTP verified for user {}", normalizedEmail);
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private void saveOtp(CustomUser user, String otp, VerificationTokenType type) {
        verificationRepository.deleteAllByTokenOwnerAndTokenType(user, type);
        VerificationEntity entity = new VerificationEntity();
        entity.setTokenOwner(user);
        entity.setToken(otp);
        entity.setTokenType(type);
        verificationRepository.save(entity);
    }
}