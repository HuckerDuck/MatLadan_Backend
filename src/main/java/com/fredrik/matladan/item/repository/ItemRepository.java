package com.fredrik.matladan.item.repository;

import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.user.model.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository <Item, Long> {
    //? Find all the items that belongs to a specific user
    //? Simple List
    List<Item> findAllByStorageOwner_Id(UUID ownerId);
    //? With pagination
    Page<Item> findAllByStorageOwner_Id(UUID ownerId, Pageable pageable);

    // Filter by Storage Spot
    List<Item> findAllByStorageOwner_IdAndStorageLocation(UUID ownerId, StorageLocation storagelocation);
    // With pagnation
    Page<Item> findAllByStorageOwner_IdAndStorageLocation(UUID ownerId, StorageLocation storagelocation, Pageable pageable);

    //? Find an item by id and storage owner id
    Optional<Item> findByIdAndStorageOwner_Id(Long itemId, UUID ownerId);

    //? Find an item by the name (typing with ignoring cases for big or lower letters)
    List<Item> findAllByStorageOwner_IdAndNameContainingIgnoreCase(UUID ownerId, String name);
    //? With pagination
    Page<Item> findAllByStorageOwner_IdAndNameContainingIgnoreCase(UUID ownerId, String name, Pageable pageable);

    //? This is used finting all the items that belongs to a specific user
    List<Item> findByStorageOwner(CustomUser storageOwner);

    List<Item> findAllByHousehold(Household household);
    Page<Item> findAllByHousehold(Household household, Pageable pageable);
    List<Item> findAllByHouseholdAndStorageLocation(Household household, StorageLocation location);
    Page<Item> findAllByHouseholdAndStorageLocation(Household household, StorageLocation location, Pageable pageable);
    List<Item> findAllByHouseholdAndNameContainingIgnoreCase(Household household, String name);
    Page<Item> findAllByHouseholdAndNameContainingIgnoreCase(Household household, String name, Pageable pageable);
    Optional<Item> findByIdAndHousehold(Long id, Household household);
}
