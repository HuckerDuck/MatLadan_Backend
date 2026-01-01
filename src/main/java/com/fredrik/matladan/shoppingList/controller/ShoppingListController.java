package com.fredrik.matladan.shoppingList.controller;

import com.fredrik.matladan.shoppingList.dto.CreateShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.PatchShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.ShoppingListResponseDTO;
import com.fredrik.matladan.shoppingList.service.ShoppingListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/api/shopping-list")
@RestController
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    //? Adding a logger to be added to this class
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ShoppingListController.class);

    //? Get all items from shopping list
    @GetMapping
    public ResponseEntity<List<ShoppingListResponseDTO>> getAllShoppingListItems(){
        List<ShoppingListResponseDTO> shoppingListItems = shoppingListService.getAllShoppingListItems();
        return ResponseEntity.ok(shoppingListItems);
    }

    //? Post mapping for creating shopping list items
    @PostMapping
    public ResponseEntity<ShoppingListResponseDTO> addItem(
            @Valid @RequestBody CreateShoppingListDTO createShoppingListDTO
    ) {
        logger.info("Adding item to shopping list: {}", createShoppingListDTO.name());
        ShoppingListResponseDTO created = shoppingListService.createShoppingList(createShoppingListDTO);
        return ResponseEntity.status(201).body(created);
    }

    //? Post mapping to create a new shopping list based on the recipe
    @PostMapping("/from-recipe/{recipeId}")
    public ResponseEntity<List<ShoppingListResponseDTO>> createShoppingListFromRecipe(
            @PathVariable Long recipeId) {
        logger.info("Creating shopping list from recipe: {}", recipeId);

        List <ShoppingListResponseDTO> shoppingList =
                shoppingListService.getShoppingListFromRecipeWithMissingIngrient(recipeId);

        logger.info("Shopping list created: {}", shoppingList.size());

        return ResponseEntity.ok(shoppingList);
    }

    //? Patch mapping for updating shopping list items
    @PatchMapping("/{id}")
    public ResponseEntity<ShoppingListResponseDTO> patchItem(
            @PathVariable Long id,
            @Valid @RequestBody PatchShoppingListDTO dto
    ) {
        logger.info("Updating shopping list item {}", id);
        ShoppingListResponseDTO updated = shoppingListService.patchShoppingListItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        logger.info("Deleting shopping list item {}", id);
        shoppingListService.deleteShoppingListItem(id);
        return ResponseEntity.noContent().build();
    }


}
