package com.fredrik.matladan.user.userdetails;

import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetailsImpl implements UserDetails {
    private final CustomUser user;


    public CustomUserDetailsImpl(CustomUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //? Spring Security handles authories in a diffrent way
        //? Therefore we need to convert our Enumss to SimpleGrantedAuthority
        //? This is done by the SimpleGrantedAuthority class
        //? Therefore we write ROLE_ and not simply ROLE

        //? Will later be used in our SecurityConfig to check the diffrent
        //? path and who has the right to be there or not
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        //! If you add a list of roles here instead.
        //! Use a .foreach authorities.addall

        //! For every role we had a new element
        //! Use a Hashset in start as well
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public CustomUser getUser() {
        return user;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
