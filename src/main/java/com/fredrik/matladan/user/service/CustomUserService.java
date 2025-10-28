package com.fredrik.matladan.user.service;

import com.fredrik.matladan.user.dto.CustomUserDTO;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService {
    @Autowired
    private CustomUserRepository customUserRepository;

    private CustomUserDTO customUserDTO;


}
