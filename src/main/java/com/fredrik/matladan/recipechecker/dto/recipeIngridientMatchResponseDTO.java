package com.fredrik.matladan.recipechecker.dto;

import java.util.List;

public record recipeIngridientMatchResponseDTO (
        Long recipeId,
        String recipeName,
        boolean canMake,
        int matchPercentage,
        List<recipeIngridientMatchDTO> ingredientMatches
) {
}
