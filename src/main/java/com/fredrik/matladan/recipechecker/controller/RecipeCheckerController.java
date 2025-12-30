package com.fredrik.matladan.recipechecker.controller;

import com.fredrik.matladan.recipechecker.dto.RecipeIngredientMatchResponseDTO;
import com.fredrik.matladan.recipechecker.service.RecipeCheckerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe-checker")
@RequiredArgsConstructor
public class RecipeCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeCheckerController.class);
    private final RecipeCheckerService recipeCheckerService;

    //? Check if current user can make a recipe
    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<RecipeIngredientMatchResponseDTO> checkRecipe(@PathVariable Long recipeId) {
        logger.info("Checking recipe: {}", recipeId);

        RecipeIngredientMatchResponseDTO result = recipeCheckerService.canMakeRecipe(recipeId);

        logger.debug("Match result - canMake: {}, matchPercentage: {}%",
                result.canMake(), result.matchPercentage());

        return ResponseEntity.ok(result);
    }
}