package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.recipechecker.dto.RecipeIngredientMatchResponseDTO;

public interface RecipeCheckerService {
    RecipeIngredientMatchResponseDTO canMakeRecipe(Long recipeId);
}
