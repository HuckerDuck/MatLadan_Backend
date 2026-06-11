package com.fredrik.matladan.security.service;

import com.fredrik.matladan.user.model.CustomUser;

public interface VerificationService {
    void sendVerificationOtp(CustomUser user);
    void sendPasswordResetOtp(CustomUser user);
    void verifyEmail(String email, String otp);
    void verifyPasswordResetOtp(String email, String otp);

}
