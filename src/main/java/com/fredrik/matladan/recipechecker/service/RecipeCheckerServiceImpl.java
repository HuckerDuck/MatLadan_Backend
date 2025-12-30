package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.recipe.repository.RecipeRepository;

import com.fredrik.matladan.recipechecker.dto.recipeIngridientMatchResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeCheckerServiceImpl implements RecipeCheckerService {

    private final RecipeRepository recipeRepository;
    private final ItemRepository itemRepository;

    @Override
    public recipeIngridientMatchResponseDTO canMakeRecipe(Long recipeId) {
        // TODO: Implement
        return null;
    }

    private CustomUser getCurrentUser() {
        return (CustomUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}