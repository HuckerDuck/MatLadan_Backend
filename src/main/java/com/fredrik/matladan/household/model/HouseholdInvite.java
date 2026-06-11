package com.fredrik.matladan.household.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "household_invites")
public class HouseholdInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    // Email address of the person being invited
    @Column(name = "invited_email", nullable = false)
    private String invitedEmail;

    // Unique token sent in the invite link
    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.expiryDate == null) {
            // Invite expires after 24 hours
            this.expiryDate = LocalDateTime.now().plusHours(24);
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
