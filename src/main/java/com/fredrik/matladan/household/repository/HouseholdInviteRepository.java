package com.fredrik.matladan.household.repository;

import com.fredrik.matladan.household.model.HouseholdInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HouseholdInviteRepository extends JpaRepository<HouseholdInvite, Long> {
    Optional<HouseholdInvite> findByToken(String token);
    void deleteAllByInvitedEmail(String email);
}
