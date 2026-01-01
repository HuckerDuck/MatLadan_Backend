package com.fredrik.matladan.shoppingList.service;

import com.fredrik.matladan.item.exceptions.UserIsNotLoggedInException;
import com.fredrik.matladan.shoppingList.dto.CreateShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.PatchShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.ShoppingListResponseDTO;
import com.fredrik.matladan.shoppingList.entity.ShoppingListEntity;
import com.fredrik.matladan.shoppingList.mapper.ShoppingListMapper;
import com.fredrik.matladan.shoppingList.repository.ShoppingListRepository;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService{
    private final CustomUserRepository customUserRepository;
    private final ShoppingListMapper shoppingListMapper;
    private final ShoppingListRepository shoppingListRepository;

    @Override
    public ShoppingListResponseDTO createShoppingList(CreateShoppingListDTO createShoppingListDTO) {
        //? Get user from SecurityContext
        //? This will throw an exception if the user is not logged in
        CustomUser currentUser = getCurrentUser();

        //? Then map from DTO to Entity
        ShoppingListEntity shoppingListEntity = shoppingListMapper.toEntity(createShoppingListDTO, currentUser);

        ShoppingListEntity savedShoppingList = shoppingListRepository.save(shoppingListEntity);

        return shoppingListMapper.toResponseDTO(savedShoppingList);
    }

    @Override
    public List<ShoppingListResponseDTO> getAllShoppingListItems() {
        CustomUser currentUser = getCurrentUser();

        List<ShoppingListEntity> entities = shoppingListRepository.findAllByUserId(currentUser.getId());

        return entities.stream()
                .map(shoppingListMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ShoppingListResponseDTO patchShoppingListItem(Long id, PatchShoppingListDTO patchShoppingListDTO) {
        ShoppingListEntity entity = shoppingListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping list item not found with id: " + id));

        shoppingListMapper.patch(patchShoppingListDTO, entity);

        ShoppingListEntity updated = shoppingListRepository.save(entity);

        return shoppingListMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteShoppingListItem(Long id) {
        if (!shoppingListRepository.existsById(id)) {
            throw new RuntimeException("Shopping list item not found with id: " + id);
        }
        shoppingListRepository.deleteById(id);
    }

    private CustomUser getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null){
            throw new RuntimeException("User is not logged in");
        }

        String username = auth.getName();

        return customUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserIsNotLoggedInException());
    }
}
