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
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser tokenOwner;

    // Stores the 6-digit OTP code
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    // Distinguishes between email verification and password reset tokens
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private VerificationTokenType tokenType;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (expirationDate == null) {
            // OTP expires after 15 minutes
            expirationDate = LocalDateTime.now().plusMinutes(15);
        }
        if (token == null) {
            throw new IllegalStateException("Token must be set before persisting");
        }
        if (tokenType == null) {
            tokenType = VerificationTokenType.VERIFICATION;
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
}