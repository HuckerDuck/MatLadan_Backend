package com.fredrik.matladan.household.service;

import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.user.model.CustomUser;

public interface HouseholdService {
    // Creates a new household and makes the user the owner
    Household createHouseholdForUser(CustomUser user);

    // Returns the household for a user — throws if not found
    Household getHouseholdForUser(CustomUser user);

    void inviteMember(CustomUser inviter, String inviteeEmail);
    void joinHousehold(CustomUser user, String token);
}
