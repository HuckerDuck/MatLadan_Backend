package com.fredrik.matladan.user.model;

import com.fredrik.matladan.user.enums.CustomUserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// This is used to the database
// This is gonna tell the database which table this needs to go into
@Table (name = "users")
public class CustomUser {

    //? ID inside the database, the database sets this so we don't need to
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter (AccessLevel.NONE)
    @Column (updatable = false, nullable = false)
    private UUID id;

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
    private Boolean enabled;

    //? Adding a way to add a roles
    //? For the User I only want it to have the User not not like an
    //? Admin role or anything like that
    @Enumerated(EnumType.STRING)
    @Column (nullable = false)

    //! Make a List of diffrent roles?
    private CustomUserRole role;

    //? When the User was created
    @Setter (AccessLevel.NONE)
    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //? When a User was updated and changed
    @Column (name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //? This is used to let the database know when a User is created
    //? Spring will automaticly fill this in
    @PrePersist
    void onCreatingUser(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;

        //? Check if enabled is null then set it to true
        //?
        //? Small change for security
        //? User will be not enabled
        if(enabled == null){
            this.enabled = false;
        }

        if (role == null){
            this.role = CustomUserRole.USER;
        }
    }

    //? This is used to let the database know when a User is updated
    //? Spring will automaticly fill this in
    @PreUpdate
    void onUpdatingUser(){
        this.updatedAt = LocalDateTime.now();
    }
}
