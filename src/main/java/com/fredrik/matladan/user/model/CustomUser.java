package com.fredrik.matladan.user.model;

import com.fredrik.matladan.user.enums.CustomUserRoleEnums;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
// This is used to the database
// This is gonna tell the database which table this needs to go into
@Table (name = "users")
public class CustomUser {

    //? ID inside the database, the database sets this so we don't need to
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //? Unique Username that is sent to the database
    @Column(unique = true, nullable = false)
    private String username;

    // Hashed password so that you can directly see what is written
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false)
    private String email;

    //? Field for letting me as a admin later disable an account
    //? No more need to delete it from the database
    //? The account will be enabled as a standard
    @Column(nullable = false)
    private Boolean enabled = true;

    //? Adding a way to add a roles
    //? For the User I only want it to have the User not not like an
    //? Admin role or anything like that
    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private CustomUserRoleEnums role = CustomUserRoleEnums.USER;

    //? When the User was created
    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //? When a User was updated and changed
    @Column (name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CustomUser(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public CustomUser(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
