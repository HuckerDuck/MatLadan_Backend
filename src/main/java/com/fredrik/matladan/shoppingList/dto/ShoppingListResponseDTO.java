package com.fredrik.matladan.shoppingList.dto;

import com.fredrik.matladan.item.enums.UnitAmountType;

import java.time.LocalDateTime;

//? No rules for @ is needed since this is just the response DTO return to the client
public record ShoppingListResponseDTO(
        Long id,
        String name,
        double quantity,
        UnitAmountType unitAmountType,
        boolean purchased,
        Long sourceRecipeId,
        String recipeName,
        LocalDateTime addedDate
){
}
