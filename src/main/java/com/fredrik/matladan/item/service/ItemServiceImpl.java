package com.fredrik.matladan.item.service;

import com.fredrik.matladan.household.model.Household;
import com.fredrik.matladan.household.service.HouseholdService;
import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;
import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.exceptions.ItemNotFoundException;
import com.fredrik.matladan.item.exceptions.UserIsNotLoggedInException;
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
public class ItemServiceImpl implements ItemService {
    private final CustomUserRepository customUserRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final HouseholdService householdService;

    @Override
    public ItemResponseDTO createItem(CreateItemDTO createItemDTO) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        Item item = itemMapper.toEntity(createItemDTO, currentUser);
        item.setHousehold(household);

        validateFridgeHasExpiryDate(item);

        return itemMapper.toResponseDTO(itemRepository.save(item));
    }

    @Override
    public List<ItemResponseDTO> getAllItemsFromCurrentUser() {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHousehold(household)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<ItemResponseDTO> getAllItemsFromCurrentUser(Pageable pageable) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHousehold(household, pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public List<ItemResponseDTO> getItemsByLocation(StorageLocation location) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHouseholdAndStorageLocation(household, location)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<ItemResponseDTO> getItemsByLocation(StorageLocation location, Pageable pageable) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHouseholdAndStorageLocation(household, location, pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public List<ItemResponseDTO> searchItemsByName(String itemName) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHouseholdAndNameContainingIgnoreCase(household, itemName)
                .stream()
                .map(itemMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Page<ItemResponseDTO> searchItemsByName(String itemName, Pageable pageable) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        return itemRepository.findAllByHouseholdAndNameContainingIgnoreCase(household, itemName, pageable)
                .map(itemMapper::toResponseDTO);
    }

    @Override
    public ItemResponseDTO updateItemFromCurrentUser(Long id, UpdateItemDTO updateItemDTO) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        Item item = itemRepository.findByIdAndHousehold(id, household)
                .orElseThrow(() -> new ItemNotFoundException(id));

        itemMapper.patch(updateItemDTO, item);
        validateFridgeHasExpiryDate(item);

        return itemMapper.toResponseDTO(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        CustomUser currentUser = getCurrentUser();
        Household household = householdService.getHouseholdForUser(currentUser);

        Item item = itemRepository.findByIdAndHousehold(id, household)
                .orElseThrow(() -> new ItemNotFoundException(id));

        itemRepository.delete(item);
    }

    private void validateFridgeHasExpiryDate(Item item) {
        if (item.getStorageLocation() == StorageLocation.FRIDGE
                && item.getExpiryDate() == null) {
            throw new IllegalArgumentException("Fridge must have an expiry date");
        }
    }

    private CustomUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("User is not logged in");
        }

        return customUserRepository.findByEmail(auth.getName())
                .orElseThrow(UserIsNotLoggedInException::new);
    }
}