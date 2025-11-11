package com.fredrik.matladan.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fredrik.matladan.user.enums.CustomUserPermissions.*;

@AllArgsConstructor
@Getter
public enum CustomUserRole {
    USER(CustomUserRoleName.USER.getRoleName(),
            Set.of(
                    READ,
                    WRITE,
                    DELETE
            )
    ),

    ADMIN(
            CustomUserRoleName.ADMIN.getRoleName(),
         Set.of(
                 READ,
                 WRITE,
                 DELETE
         )
    );

    private final String roleName;
    private final Set<CustomUserPermissions> userPermissions;


    public List<SimpleGrantedAuthority> getUserAuthorities() {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(this.roleName));
        authorityList.addAll(
                this.userPermissions.stream().map(
                        userPermission ->
                                new SimpleGrantedAuthority
                                        (userPermission.getCustomUserPermission())
                ).toList()
        );

        return authorityList;
    }

}
