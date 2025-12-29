package com.fredrik.matladan.recipe.model;

import com.fredrik.matladan.item.enums.UnitAmountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "recipe_ingredients")
@Getter
@Setter
public class RecipeIngredient {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //? Many RecipeIngredients can belong to one Recipe
    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(nullable = false)
    private double amount;


    @Column(name = "unit_amount_type")
    @Enumerated(EnumType.STRING)
    private UnitAmountType unitAmountType;
}
