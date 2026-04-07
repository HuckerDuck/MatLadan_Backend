package com.fredrik.matladan.item.service;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;
import com.fredrik.matladan.item.enums.StorageLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
    //? Create a new Item
    ItemResponseDTO createItem(CreateItemDTO createItemDTO);
    //? Get all items from a specific user
    List<ItemResponseDTO> getAllItemsFromCurrentUser();
    Page<ItemResponseDTO> getAllItemsFromCurrentUser(Pageable pageable);

    //? Get all items by the Location
    List<ItemResponseDTO> getItemsByLocation(StorageLocation location);
    Page<ItemResponseDTO> getItemsByLocation(StorageLocation location, Pageable pageable);

    //? Get all items by the name
    List<ItemResponseDTO> searchItemsByName(String itemName);
    Page<ItemResponseDTO> searchItemsByName(String itemName, Pageable pageable);

    //? Update an item
    ItemResponseDTO updateItemFromCurrentUser(Long id, UpdateItemDTO updateItemDTO);

    //? Delete an item
    void deleteItem(Long id);

    //? Get all items

}
