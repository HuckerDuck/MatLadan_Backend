package com.fredrik.matladan.recipe.service;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.dto.RecipeResponseDTO;
import com.fredrik.matladan.recipe.exceptions.RecipeNotFoundException;
import com.fredrik.matladan.recipe.mapper.RecipeMapper;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    //? Post - Create a new recipe
    @Override
    public CreateRecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO) {
        RecipeEntity recipe = recipeMapper.toEntity(createRecipeDTO);
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.toDTO(savedRecipe);
    }

    //? Read all
    @Override
    public List<RecipeResponseDTO> getAllRecipes() {
        List<RecipeEntity> recipes = recipeRepository.findAll();
        return recipeMapper.toResponseDTOList(recipes);
    }

    @Override
    public Page<RecipeResponseDTO> getAllRecipesPaginated(Pageable pageable) {
        Page<RecipeEntity> recipePage = recipeRepository.findAll(pageable);
        return recipePage.map(recipeMapper::toResponseDTO);
    }

    //? Get - Get a recipe by id
    @Override
    public RecipeResponseDTO getRecipeById(Long id) {
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return recipeMapper.toResponseDTO(recipe);
    }

    //? Delete

}