package com.fredrik.matladan.recipechecker.service;

import com.fredrik.matladan.item.repository.ItemRepository;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.repository.RecipeRepository;
import com.fredrik.matladan.recipechecker.dto.recipeIngridientMatchResponseDTO;
import com.fredrik.matladan.user.model.CustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeCheckerServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RecipeCheckerServiceImpl recipeCheckerService;

    //? The Setup before the test
    @BeforeEach
    void setUp() {
        CustomUser testUser = new CustomUser();
        testUser.setUsername("testuser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        SecurityContextHolder.setContext(securityContext);
    }

    //? Test 1 to see if the user can make the recipe
    @Test
    void canTheUserMakeTheRecipe() {
        // Arrange
        RecipeEntity recipe = new RecipeEntity();
        recipe.setName("Test Recipe");
        recipe.setIngredients(new ArrayList<>());

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(itemRepository.findByStorageOwner(any())).thenReturn(new ArrayList<>());

        // Act
        recipeIngridientMatchResponseDTO result = recipeCheckerService.canMakeRecipe(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Recipe", result.recipeName());
    }
}