package com.fredrik.matladan.item.repository;

import com.fredrik.matladan.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository <Item, Long> {
    Optional<Item> findItemByIdAndWithStorageOwnerID(Long id, UUID storageOwnerID);
}
