package com.fredrik.matladan.shoppingList.dto;

import com.fredrik.matladan.item.enums.UnitAmountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//? Adding the DTO for creating a shopping list
//? This will be used when creating a shopping list from a recipe
public record CreateShoppingListDTO (
        //? We don't wanna send empty field so we add @NotBlank and @NotNull
        @NotBlank
        String name,

        @NotNull
        double quantity,

        @NotNull
        UnitAmountType unitAmountType,
        Long sourceRecipeId
){
}
