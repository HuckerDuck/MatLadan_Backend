package com.fredrik.matladan.household.controller;

import com.fredrik.matladan.household.service.HouseholdService;
import com.fredrik.matladan.item.exceptions.UserIsNotLoggedInException;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/households")
@RequiredArgsConstructor
public class HouseHoldController {
    private final HouseholdService householdService;
    private final CustomUserRepository customUserRepository;

    /**
     * Invite someone to your household by email.
     * The invitee receives an email with a join token.
     */
    @PostMapping("/invite")
    public ResponseEntity<Map<String, String>> inviteMember(
            @RequestBody InviteRequest request
    ) {
        CustomUser currentUser = getCurrentUser();
        householdService.inviteMember(currentUser, request.email());

        return ResponseEntity.ok(Map.of(
                "message", "Invite sent to " + request.email()
        ));
    }

    /**
     * Join a household using the token from an invite email.
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinHousehold(
            @RequestBody JoinRequest request
    ) {
        CustomUser currentUser = getCurrentUser();
        householdService.joinHousehold(currentUser, request.token());

        return ResponseEntity.ok(Map.of(
                "message", "You have joined the household successfully."
        ));
    }

    private CustomUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("User is not logged in");
        }
        return customUserRepository.findByEmail(auth.getName())
                .orElseThrow(UserIsNotLoggedInException::new);
    }

    record InviteRequest(
            @NotBlank @Email String email
    ) {}

    record JoinRequest(
            @NotBlank String token
    ) {}
}
