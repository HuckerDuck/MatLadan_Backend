package com.fredrik.matladan.shoppingList.service;

import com.fredrik.matladan.shoppingList.dto.CreateShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.PatchShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.ShoppingListResponseDTO;

import java.util.List;

public class ShoppingListServiceImpl implements ShoppingListService{
    @Override
    public ShoppingListResponseDTO createShoppingList(CreateShoppingListDTO createShoppingListDTO) {
        return null;
    }

    @Override
    public List<ShoppingListResponseDTO> getAllShoppingListItems() {
        return List.of();
    }

    @Override
    public ShoppingListResponseDTO patchShoppingListItem(Long id, PatchShoppingListDTO patchShoppingListDTO) {
        return null;
    }

    @Override
    public void deleteShoppingListItem(Long id) {

    }
}
