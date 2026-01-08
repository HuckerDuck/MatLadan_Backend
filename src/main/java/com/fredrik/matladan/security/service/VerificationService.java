package com.fredrik.matladan.security.service;

import com.fredrik.matladan.user.model.CustomUser;

public interface VerificationService {

    //? Method for adding token to the user
    String createTokenForUser (CustomUser user);

}
