package com.fredrik.matladan.security.repository;

import com.fredrik.matladan.security.VerificationEntity.VerificationEntity;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository <VerificationEntity, Long > {
    Optional <VerificationEntity> findByToken(String token);

    long deleteAllByTokenOwner(CustomUser user);
}
