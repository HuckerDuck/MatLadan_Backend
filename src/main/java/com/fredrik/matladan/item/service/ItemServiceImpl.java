package com.fredrik.matladan.item.service;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;
import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.mapper.ItemMapper;
import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService{
    private final CustomUserRepository customUserRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(CustomUserRepository customUserRepository, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.customUserRepository = customUserRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }


    @Override
    public ItemResponseDTO createItem(CreateItemDTO createItemDTO) {
        //? Get user från SecurityContext
        //? This will throw an exception if the user is not logged in
        CustomUser currentUser = getCurrentUser();

        //? Then map from DTO to Entity
        Item item = itemMapper.toEntity(createItemDTO, currentUser);

        //? Add the helping method to make sure that the fridge items has an expiry date
        validateFridgeHasExpirydate(item);

        //? Save the item to the database
        Item savedItem = itemRepository.save(item);

        //? Then map the Entity to a ResponseDTO
        return itemMapper.toResponseDTO(savedItem);
    }

    @Override
    public UpdateItemDTO updateItem(Long id, UpdateItemDTO updateItemDTO) {
        return null;
    }


    @Override
    public void deleteItem(Long id) {

    }

    //? Helping method to make sure that the fridge items has an expiry date
    private void validateFridgeHasExpirydate(Item item){
        if (item.getStorageLocation() == StorageLocation.FRIDGE
                && item.getExpiryDate() == null) {
            throw new IllegalArgumentException("Fridge must have an expiry date");
        }
    }

    //? Helping method from SecurityContext
    //? To make sure that there is a user logged in
    private CustomUser getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null){
            throw new RuntimeException("User is not logged in");
        }

        String username = auth.getName();

        return customUserRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException("Can't find user in the database with username " + username));
    }
}
