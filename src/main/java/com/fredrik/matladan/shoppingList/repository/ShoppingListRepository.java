package com.fredrik.matladan.shoppingList.repository;

import com.fredrik.matladan.item.enums.UnitAmountType;
import com.fredrik.matladan.shoppingList.entity.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingListRepository extends JpaRepository <ShoppingListEntity, Long> {
    List<ShoppingListEntity> findAllByUserId(UUID userId);

    //?
    Optional<ShoppingListEntity> findByIdAndUserId(Long id, UUID userId);

    //? Method for finding items that are duplicated
    //? Needed so that I don't duplicate items when I'm creating a new shoppingList
    //? Find a items by the Name and the UnitAmoutType
    //? Uses the UserID and finds the list of items that are not purches with the boolean being false
    Optional<ShoppingListEntity> findByNameAndUnitAmountTypeAndUserIdAndPurchasedFalse(
            String name,
            UnitAmountType unitAmountType,
            UUID userId
    );

}
