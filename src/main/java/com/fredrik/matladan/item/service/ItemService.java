package com.fredrik.matladan.item.service;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;

public interface ItemService {
    //? Create a new Item
    ItemResponseDTO createItem(CreateItemDTO createItemDTO);

    //? Update an item
    UpdateItemDTO updateItem(Long id, UpdateItemDTO updateItemDTO);

    //? Delete an item
    void deleteItem(Long id);
}
