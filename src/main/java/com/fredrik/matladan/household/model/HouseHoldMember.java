package com.fredrik.matladan.household.model;

import com.fredrik.matladan.household.role.HouseHoldRoleENUM;
import com.fredrik.matladan.user.model.CustomUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "household_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"household_id", "user_id"}))
public class HouseHoldMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HouseHoldRoleENUM role;

    @Setter(AccessLevel.NONE)
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    void onCreate() {
        this.joinedAt = LocalDateTime.now();
    }
}