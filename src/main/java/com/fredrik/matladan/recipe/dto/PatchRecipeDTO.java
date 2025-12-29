package com.fredrik.matladan.recipe.dto;

import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PatchRecipeDTO(
        @Size(min = 3, max = 100, message = "Recipe name must be between 3 and 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        String instructions,

        String imageURL,

        @Positive(message = "Servings must be positive")
        Integer servings,

        @PositiveOrZero(message = "Prep time must be zero or positive")
        Integer prepTime,

        @PositiveOrZero(message = "Cook time must be zero or positive")
        Integer cookTime,

        MealType mealType,

        DietType dietType,

        @Valid
        List<RecipeIngredientDTO> ingredients
) {
}