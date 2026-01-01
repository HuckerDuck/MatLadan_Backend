package com.fredrik.matladan.shoppingList.dto;

public record PatchShoppingListDTO (
        //? boolean = true if purchased, false if not
        //? Boolean = true or false and also can be null
        //? Same with double compared to Double
        Boolean isPurchased,
        Double quantity
) {
}
