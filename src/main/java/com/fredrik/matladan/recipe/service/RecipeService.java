package com.fredrik.matladan.recipe.service;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.dto.RecipeResponseDTO;
import com.fredrik.matladan.recipe.dto.PatchRecipeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {

    //? Basic CRUD to start with and then we can test all this
    //?
    //? Create a new Recipe
    CreateRecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO);

    //? Get all Recipes
    List<RecipeResponseDTO> getAllRecipes();
    Page<RecipeResponseDTO> getAllRecipesPaginated(Pageable pageable);

    //? Patch a recipe
    RecipeResponseDTO patchRecipe(Long id, PatchRecipeDTO patchRecipeDTO);

    //? Delete a Recipe
    void deleteRecipe(Long id);

    //? Get a Recipe by ID
    RecipeResponseDTO getRecipeById(Long id);


}
