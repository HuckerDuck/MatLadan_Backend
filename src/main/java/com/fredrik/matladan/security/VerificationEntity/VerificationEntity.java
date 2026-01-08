package com.fredrik.matladan.security.VerificationEntity;

import com.fredrik.matladan.user.model.CustomUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "verification_token")
public class VerificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter (AccessLevel.NONE)
    @Column (updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser tokenOwner;

    @Column(name = "token", nullable = false, unique = true)
    private String token;


    @Column (name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column (name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }

        if (expirationDate == null) {
            //? Setting the expiration date of the token to be from now and 24 hours ahead
            //? After that the token should expire
            expirationDate = LocalDateTime.now().plusHours(2);
        }

        if (token == null) {
            throw  new IllegalStateException("No token has been set" + "The token is empty");
        }


    }


}
