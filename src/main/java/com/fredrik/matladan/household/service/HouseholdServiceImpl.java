package com.fredrik.matladan.household.service;

import com.fredrik.matladan.household.model.HouseHoldMember;
import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.household.model.HouseholdInvite;
import com.fredrik.matladan.household.repository.HouseHoldRepository;
import com.fredrik.matladan.household.repository.HouseholdInviteRepository;
import com.fredrik.matladan.household.repository.HouseholdMemberRepository;
import com.fredrik.matladan.household.role.HouseHoldRoleENUM;
import com.fredrik.matladan.security.service.EmailService;
import com.fredrik.matladan.user.model.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseholdServiceImpl implements HouseholdService{
    private static final Logger logger = LoggerFactory.getLogger(HouseholdServiceImpl.class);

    private final HouseHoldRepository householdRepository;
    private final HouseholdMemberRepository householdMemberRepository;
    private final HouseholdInviteRepository householdInviteRepository;
    private final EmailService emailService;

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

    @Override
    @Transactional
    public void inviteMember(CustomUser inviter, String inviteeEmail) {
        String normalizedEmail = inviteeEmail.toLowerCase().trim();

        // Get inviter's household
        Household household = getHouseholdForUser(inviter);

        // Delete any existing invite for this email to this household
        householdInviteRepository.deleteAllByInvitedEmail(normalizedEmail);

        // Create invite token
        HouseholdInvite invite = new HouseholdInvite();
        invite.setHousehold(household);
        invite.setInvitedEmail(normalizedEmail);
        invite.setToken(String.format("%06d", new java.security.SecureRandom().nextInt(1_000_000)));
        householdInviteRepository.save(invite);

        // Send invite email
        emailService.sendHouseholdInvite(
                normalizedEmail,
                inviter.getEmail(),
                household.getName(),
                invite.getToken()
        );

        logger.info("Invite sent to {} for household {}", normalizedEmail, household.getId());
    }

    @Override
    @Transactional
    public void joinHousehold(CustomUser user, String token) {
        HouseholdInvite invite = householdInviteRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired invite token"));

        if (invite.isExpired()) {
            householdInviteRepository.delete(invite);
            throw new IllegalArgumentException("Invite has expired. Please request a new one.");
        }

        if (!invite.getInvitedEmail().equalsIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException("This invite was sent to a different email address.");
        }

        // Remove user from their current household
        householdMemberRepository.findByUser(user)
                .ifPresent(householdMemberRepository::delete);

        // Add user to the new household as MEMBER
        HouseHoldMember member = new HouseHoldMember();
        member.setHousehold(invite.getHousehold());
        member.setUser(user);
        member.setRole(HouseHoldRoleENUM.MEMBER);
        householdMemberRepository.save(member);

        // Delete the used invite
        householdInviteRepository.delete(invite);

        logger.info("User {} joined household {}", user.getEmail(), invite.getHousehold().getId());
    }

}
