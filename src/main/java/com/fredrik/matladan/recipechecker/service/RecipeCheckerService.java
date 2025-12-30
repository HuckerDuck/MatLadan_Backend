package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.recipechecker.dto.recipeIngridientMatchResponseDTO;

public interface RecipeCheckerService {
    recipeIngridientMatchResponseDTO canMakeRecipe(Long recipeId);
}
