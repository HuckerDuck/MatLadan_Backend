package com.fredrik.matladan.household.service;

import com.fredrik.matladan.household.model.HouseHoldMember;
import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.household.repository.HouseHoldRepository;
import com.fredrik.matladan.household.repository.HouseholdMemberRepository;
import com.fredrik.matladan.household.role.HouseHoldRoleENUM;
import com.fredrik.matladan.user.model.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HouseholdServiceImpl implements HouseholdService{
    private static final Logger logger = LoggerFactory.getLogger(HouseholdServiceImpl.class);

    private final HouseHoldRepository householdRepository;
    private final HouseholdMemberRepository householdMemberRepository;

    @Override
    @Transactional
    public Household createHouseholdForUser(CustomUser user) {
        // Create the household — named after the user's email by default
        Household household = new Household();
        household.setName(user.getEmail() + "'s household");
        Household saved = householdRepository.save(household);

        // Make the user the owner
        HouseHoldMember member = new HouseHoldMember();
        member.setHousehold(saved);
        member.setUser(user);
        member.setRole(HouseHoldRoleENUM.OWNER);
        householdMemberRepository.save(member);

        logger.info("Created household for user {}", user.getEmail());
        return saved;
    }

    @Override
    public Household getHouseholdForUser(CustomUser user) {
        return householdMemberRepository.findByUser(user)
                .map(HouseHoldMember::getHousehold)
                .orElseThrow(() -> new RuntimeException("No household found for user " + user.getEmail()));
    }

}
