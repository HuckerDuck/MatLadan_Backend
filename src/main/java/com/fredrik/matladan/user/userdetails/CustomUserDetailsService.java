package com.fredrik.matladan.user.userdetails;

import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//! Remember putting the bean @Service

/*
* Loads the User from the databasse
* Implements Business Logic & Error handling
* Preferably through an Advice klass
* */
@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
    private final CustomUserRepository repository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //? Load user from database by username
        //? Throw exception if not found - Spring Security handles this
        //? Wrap in CustomUserDetails for Spring Security

        CustomUser customUser = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username " + username + " was not found"
                ));

        return new CustomUserDetailsImpl(customUser);
    }
}
