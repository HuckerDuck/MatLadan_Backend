package com.fredrik.matladan.security.repository;

import com.fredrik.matladan.security.VerificationEntity.VerificationEntity;
import com.fredrik.matladan.security.VerificationEntity.VerificationTokenType;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository <VerificationEntity, Long > {
    Optional<VerificationEntity> findByToken(String token);
    Optional<VerificationEntity> findByTokenAndTokenType(String token, VerificationTokenType tokenType);
    Optional<VerificationEntity> findByTokenOwnerAndTokenType(CustomUser user, VerificationTokenType tokenType);
    long deleteAllByTokenOwner(CustomUser user);
    void deleteAllByTokenOwnerAndTokenType(CustomUser user, VerificationTokenType tokenType);
}
