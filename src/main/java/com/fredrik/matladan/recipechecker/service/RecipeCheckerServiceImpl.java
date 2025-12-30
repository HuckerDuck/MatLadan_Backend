package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.recipe.exceptions.RecipeNotFoundException;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.repository.RecipeRepository;

import com.fredrik.matladan.recipechecker.dto.recipeIngridientMatchDTO;
import com.fredrik.matladan.recipechecker.dto.recipeIngridientMatchResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeCheckerServiceImpl implements RecipeCheckerService {

    private final RecipeRepository recipeRepository;
    private final ItemRepository itemRepository;

    @Override
    public recipeIngridientMatchResponseDTO canMakeRecipe(Long recipeId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        CustomUser currentUser = getCurrentUser();
        List<Item> userItems = itemRepository.findByStorageOwner(currentUser);
        List<recipeIngridientMatchDTO> ingredientMatches = new ArrayList<>();


        return new recipeIngridientMatchResponseDTO(
                recipe.getId(),
                recipe.getName(),
                false,
                0,
                ingredientMatches
        );
    }

    private CustomUser getCurrentUser() {
        return (CustomUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}