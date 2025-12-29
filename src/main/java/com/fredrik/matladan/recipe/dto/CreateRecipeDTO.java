package com.fredrik.matladan.recipe.dto;

import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record CreateRecipeDTO(
        Long id,

        @NotBlank(message = "Recipe name cannot be blank")
        @Size(min = 3, max = 100, message = "Recipe name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotBlank(message = "Instructions cannot be blank")
        String instructions,

        @NotBlank(message = "Image URL cannot be blank")
        String imageURL,

        @NotNull(message = "Servings cannot be null")
        @Positive(message = "Servings must be positive")
        Integer servings,

        @NotNull(message = "Prep time cannot be null")
        @PositiveOrZero(message = "Prep time must be zero or positive")
        Integer prepTime,

        @NotNull(message = "Cook time cannot be null")
        @PositiveOrZero(message = "Cook time must be zero or positive")
        Integer cookTime,

        @NotNull(message = "Meal type cannot be null")
        MealType mealType,

        @NotNull(message = "Diet type cannot be null")
        DietType dietType,

        @NotEmpty(message = "Recipe must have at least one ingredient")
        @Valid
        List<RecipeIngredientDTO> ingredients,

        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
