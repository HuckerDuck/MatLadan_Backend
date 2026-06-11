package com.fredrik.matladan.household.repository;

import com.fredrik.matladan.household.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HouseHoldRepository extends JpaRepository<Household, UUID> {
}
