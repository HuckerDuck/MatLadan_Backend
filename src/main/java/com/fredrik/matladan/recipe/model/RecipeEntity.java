package com.fredrik.matladan.recipe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecipeEntity {
    @Setter (AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    private String name;

    private String description;

    private String instructions;

    private String imageURL;

    private Integer servings;

    private Integer prepTime;

    private Integer cookTime;





}
