package com.fredrik.matladan.user.repository;

import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomUserRepository extends CrudRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);

}
