package com.fredrik.matladan.user.userdetails;

import com.fredrik.matladan.user.enums.CustomUserRoleEnums;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final CustomUser user;
    private CustomUserRoleEnums role;

    public CustomUserDetails(CustomUser user) {
        this.user = user;
    }

    /**
     * Might later wanna add a List of Userrole Authorities
     * Like a User jave Role + Permissions [ROLE_USER, READ_USER, WRITE_USER]
     * List<SimpleGrantedAuthority> authorities;
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //? Spring Security handles authories in a diffrent way
        //? Therefore we need to convert our Enumss to SimpleGrantedAuthority
        //? This is done by the SimpleGrantedAuthority class
        //? Therefore we write ROLE_ and not simply ROLE

        //? Will later be used in our SecurityConfig to check the diffrent
        //? path and who has the right to be there or not

        //? Can also be used in the controller  with
        //? @PreAuthorize("hasRole('ROLE_USER')")
        //? Then we can check if the user has the right to do something
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getEnabled();
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
        return UserDetails.super.isEnabled();
    }
}
