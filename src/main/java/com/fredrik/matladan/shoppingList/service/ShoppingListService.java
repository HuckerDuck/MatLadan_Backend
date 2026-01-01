package com.fredrik.matladan.shoppingList.service;

import com.fredrik.matladan.recipe.dto.RecipeResponseDTO;
import com.fredrik.matladan.shoppingList.dto.CreateShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.PatchShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.ShoppingListResponseDTO;

import java.util.List;

public interface ShoppingListService {
    //? Create a new ShoppingList
    ShoppingListResponseDTO createShoppingList(CreateShoppingListDTO createShoppingListDTO);

    //? Get all ShoppingLists from a specific user
    List<ShoppingListResponseDTO> getAllShoppingListItems();

    //? Update a ShoppingList
    //? Can be used to change the quantity of an item in the list or change purches to true
    ShoppingListResponseDTO patchShoppingListItem(Long id, PatchShoppingListDTO patchShoppingListDTO);

    //? Delete a ShoppingList
    void deleteShoppingListItem(Long id);



}
