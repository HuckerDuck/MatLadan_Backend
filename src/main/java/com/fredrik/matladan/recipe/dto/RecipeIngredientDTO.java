package com.fredrik.matladan.recipe.dto;

import com.fredrik.matladan.item.enums.UnitAmountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecipeIngredientDTO(
        @NotBlank(message = "Ingredient name cannot be blank")
        String ingredientName,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        Double amount,

        @NotNull(message = "Unit type cannot be null")
        UnitAmountType unitAmountType
) {
}
