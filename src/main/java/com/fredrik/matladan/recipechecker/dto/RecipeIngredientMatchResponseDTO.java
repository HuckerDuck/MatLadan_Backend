package com.fredrik.matladan.recipechecker.dto;

import java.util.List;

public record RecipeIngredientMatchResponseDTO(
        Long recipeId,
        String recipeName,
        boolean canMake,
        int matchPercentage,
        List<RecipeIngredientMatchDTO> ingredientMatches
) {
}
