package com.fredrik.matladan.security.refreshtoken.repository;

import com.fredrik.matladan.security.refreshtoken.RefreshToken;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(CustomUser user);
}
