package com.fredrik.matladan.security.service;


import com.fredrik.matladan.security.VerificationEntity.VerificationEntity;
import com.fredrik.matladan.security.repository.VerificationRepository;
import com.fredrik.matladan.user.exceptions.UserAlreadyExistsException;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerificationServiceImpl implements VerificationService {
    private final VerificationRepository verificationRepository;

    //? Create a new verificationToken for the user
    @Override
    public String createTokenForUser(CustomUser user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot find the User");
        }

        //? Delete the old token that already exist when creating a new token
        verificationRepository.deleteAllByTokenOwner(user);

        //? Create a new token that will be used
        String token = UUID.randomUUID().toString();

        //? Create and save the token entity
        VerificationEntity verificationEntity = new VerificationEntity();
        verificationEntity.setTokenOwner(user);
        verificationEntity.setToken(token);

        verificationRepository.save(verificationEntity);

        return token;
    }
}
