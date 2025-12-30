package com.fredrik.matladan.recipechecker.dto;

import com.fredrik.matladan.item.enums.UnitAmountType;

//? Record for the response body
//? This is used to check if the recipe is possible to make
//? If it is possible, then the missingAmount will be 0
//? otherwise it will be the amount that is missing
//? boolean isAvailable is used to check if the ingredient is available
//? this will be true or false
public record recipeIngridientMatchDTO(
        String ingredientName,
        double requiredAmount,
        UnitAmountType requiredUnit,
        double availableAmount,
        double missingAmount,
        boolean isAvailable
) {
}
