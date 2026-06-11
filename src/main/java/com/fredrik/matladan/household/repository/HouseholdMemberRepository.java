package com.fredrik.matladan.household.repository;

import com.fredrik.matladan.household.model.HouseHoldMember;
import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface HouseholdMemberRepository extends JpaRepository<HouseHoldMember, Long>{
    // Find which household a user belongs to
    Optional<HouseHoldMember> findByUser(CustomUser user);

    // Find all members of a household
    List<HouseHoldMember> findAllByHousehold(Household household);

    // Check if user is already in a household
    boolean existsByUser(CustomUser user);
}
