package com.fredrik.matladan.recipe.controller;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.dto.RecipeResponseDTO;
import com.fredrik.matladan.recipe.dto.PatchRecipeDTO;
import com.fredrik.matladan.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<RecipeResponseDTO> getRecipeById(@PathVariable Long id) {
        logger.debug("Getting recipe by id: {}", id);
        RecipeResponseDTO recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping
    public ResponseEntity<Page<RecipeResponseDTO>> getAllRecipes(
            //? Pagination
            //? Default values are set so that the user have to do anything
            //? This will just return all recipes
            //? But with a set page and size
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        //? This sort is made so that the recipes are sorted by name ascending by default
        //? It's a nice way to see all recipes at once
        Sort sort = sortDirection.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        //? Then we create a pageable object with the sort and page number and size
        //? This is used to paginate the recipes
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(recipeService.getAllRecipesPaginated(pageable));
    }

    //? Patch - Update a recipe
    @PatchMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> patchRecipe(
            @PathVariable Long id,
            @Valid @RequestBody PatchRecipeDTO patchRecipeDTO
    ) {
        logger.info("Patching recipe with id: {}", id);
        RecipeResponseDTO patchedRecipe = recipeService.patchRecipe(id, patchRecipeDTO);
        return ResponseEntity.ok(patchedRecipe);
    }

    //? Delete - Delete a recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        logger.info("Deleting recipe with id: {}", id);
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }


}