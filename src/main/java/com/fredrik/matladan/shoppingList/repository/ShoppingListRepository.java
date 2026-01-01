package com.fredrik.matladan.shoppingList.repository;

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

}
