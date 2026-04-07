package com.fredrik.matladan.recipe.exceptions;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(Long id) {
        super("Recipe with id " + id + " not found");
    }
}