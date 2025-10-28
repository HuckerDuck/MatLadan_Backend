package com.fredrik.matladan.user.repository;

import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
    Optional<CustomUser> findByEmail(String email);
}
