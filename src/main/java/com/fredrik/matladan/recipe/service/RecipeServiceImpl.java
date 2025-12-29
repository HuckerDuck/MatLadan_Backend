package com.fredrik.matladan.recipe.service;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.exceptions.RecipeNotFoundException;
import com.fredrik.matladan.recipe.mapper.RecipeMapper;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public CreateRecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO) {
        RecipeEntity recipe = recipeMapper.toEntity(createRecipeDTO);
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.toDTO(savedRecipe);
    }

    @Override
    public CreateRecipeDTO getRecipeById(Long id) {
        RecipeEntity recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return recipeMapper.toDTO(recipe);
    }
}