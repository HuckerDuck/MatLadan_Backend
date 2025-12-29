package com.fredrik.matladan.recipe.service;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;

public interface RecipeService {

    //? Basic CRUD to start with and then we can test all this
    CreateRecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO);
    CreateRecipeDTO getRecipeById(Long id);
}