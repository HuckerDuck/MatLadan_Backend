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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService{
    private final CustomUserRepository customUserRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;


    @Override
    public ItemResponseDTO createItem(CreateItemDTO createItemDTO) {
        //? Get user från SecurityContext
        //? This will throw an exception if the user is not logged in
        CustomUser currentUser = getCurrentUser();

        //? Then map from DTO to Entity
        Item item = itemMapper.toEntity(createItemDTO, currentUser);

        //? Add the helping method to make sure that the fridge items has an expiry date
        validateFridgeHasExpiryDate(item);

        //? Save the item to the database
        Item savedItem = itemRepository.save(item);

        //? Then map the Entity to a ResponseDTO
        return itemMapper.toResponseDTO(savedItem);
    }

    //? Get all items from the current user who is logged in
    @Override
    public List<ItemResponseDTO> getAllItemsFromCurrentUser() {
        //? Get the current user from SecurityContext
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_Id(currentUser.getId())
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    //? Same as above but with pagination
    @Override
    public Page<ItemResponseDTO> getAllItemsFromCurrentUser(Pageable pageable) {
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_Id(currentUser.getId(), pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public List<ItemResponseDTO> getItemsByLocation(StorageLocation location) {
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_IdAndStorageLocation(currentUser.getId(), location)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<ItemResponseDTO> getItemsByLocation(StorageLocation location, Pageable pageable) {
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_IdAndStorageLocation(currentUser.getId(), location, pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public List<ItemResponseDTO> searchItemsByName(String itemName) {
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_IdAndNameContainingIgnoreCase(currentUser.getId(), itemName)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<ItemResponseDTO> searchItemsByName(String itemName, Pageable pageable) {
        CustomUser currentUser = getCurrentUser();

        return itemRepository.findAllByStorageOwner_IdAndNameContainingIgnoreCase(currentUser.getId(), itemName, pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public UpdateItemDTO updateItemFromCurrentUser(Long id, UpdateItemDTO updateItemDTO) {
        CustomUser currentUser = getCurrentUser();

        Item item = itemRepository.findByIdAndStorageOwner_Id(id, currentUser.getId()).
                orElseThrow(()
                        -> new RuntimeException("Can't find the item in with your id that you tried to update"));

        itemMapper.patch(updateItemDTO, item);

        //? Check if it's a fridge item and if it needs an expiry date
        validateFridgeHasExpiryDate(item);

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toUpdateDTO(updatedItem);
    }

    //? Delete item from the database with the id from the item
    @Override
    public void deleteItem(Long id) {
        CustomUser currentUser = getCurrentUser();

        Item item = itemRepository.findByIdAndStorageOwner_Id(id, currentUser.getId()).
                orElseThrow(()
                        -> new RuntimeException("Can't find the item in with your id that you tried to delete"));

        itemRepository.delete(item);
    }


    //
    //                         ----- Helper methods  ----
    //
    //

    //? Helping method to make sure that the fridge items has an expiry date
    private void validateFridgeHasExpiryDate(Item item){
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
