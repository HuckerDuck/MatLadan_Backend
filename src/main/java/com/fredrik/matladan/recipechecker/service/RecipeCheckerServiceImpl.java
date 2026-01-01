package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.recipe.exceptions.RecipeNotFoundException;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.model.RecipeIngredient;
import com.fredrik.matladan.recipe.repository.RecipeRepository;
import com.fredrik.matladan.recipechecker.dto.RecipeIngredientMatchDTO;
import com.fredrik.matladan.recipechecker.dto.RecipeIngredientMatchResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import com.fredrik.matladan.user.userdetails.CustomUserDetailsImpl;
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
    public RecipeIngredientMatchResponseDTO canMakeRecipe(Long recipeId) {
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(recipeId));

        CustomUser currentUser = getCurrentUser();
        List<Item> userItems = itemRepository.findByStorageOwner(currentUser);

        //? List to store the matching result for each ingredient
        List<RecipeIngredientMatchDTO> ingredientMatches = new ArrayList<>();
        //? Counter to track how many ingredients user has
        int availableCount = 0;

        //? Loop through each ingredient in the recipe
        for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
            String ingredientName = recipeIngredient.getIngredientName();
            double requiredAmount = recipeIngredient.getAmount();

            //? Try to find matching item in user's inventory
            //? Match by name (case-insensitive) and unit type
            Item matchingItem = userItems.stream()
                    .filter(item -> item.getName().equalsIgnoreCase(ingredientName))
                    .filter(item -> item.getUnitAmountType() == recipeIngredient.getUnitAmountType())
                    .findFirst()
                    .orElse(null);

            if (matchingItem == null) {
                //? User doesn't have this ingredient at all
                ingredientMatches.add(new RecipeIngredientMatchDTO(
                        ingredientName,
                        requiredAmount,
                        recipeIngredient.getUnitAmountType(),
                        0.0,
                        requiredAmount,
                        false
                ));
            } else {
                //? Calculate total available amount
                double availableAmount = matchingItem.getSizeOfUnit() * matchingItem.getQuantity();

                if (availableAmount >= requiredAmount) {
                    //? Does the user has enough of this ingredient
                    ingredientMatches.add(new RecipeIngredientMatchDTO(
                            ingredientName,
                            requiredAmount,
                            recipeIngredient.getUnitAmountType(),
                            availableAmount,
                            0.0,
                            true
                    ));
                    availableCount++;
                } else {
                    //? User has some but not enough
                    double missing = requiredAmount - availableAmount;
                    ingredientMatches.add(new RecipeIngredientMatchDTO(
                            ingredientName,
                            requiredAmount,
                            recipeIngredient.getUnitAmountType(),
                            availableAmount,
                            missing,
                            false
                    ));
                }
            }
        }

        //? Calculate final results
        int totalIngredients = recipe.getIngredients().size();
        boolean canMake = (availableCount == totalIngredients);
        int matchPercentage = totalIngredients > 0
                ? (availableCount * 100) / totalIngredients
                : 0;

        return new RecipeIngredientMatchResponseDTO(
                recipe.getId(),
                recipe.getName(),
                canMake,
                matchPercentage,
                ingredientMatches
        );
    }

    private CustomUser getCurrentUser() {
        CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
}