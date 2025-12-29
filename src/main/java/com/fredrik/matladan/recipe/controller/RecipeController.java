package com.fredrik.matladan.recipe.controller;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<CreateRecipeDTO> createRecipe(@Valid @RequestBody CreateRecipeDTO createRecipeDTO) {
        logger.info("Creating new recipe: {}", createRecipeDTO.name());
        CreateRecipeDTO response = recipeService.createRecipe(createRecipeDTO);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateRecipeDTO> getRecipeById(@PathVariable Long id) {
        logger.debug("Getting recipe by id: {}", id);
        CreateRecipeDTO recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }
}