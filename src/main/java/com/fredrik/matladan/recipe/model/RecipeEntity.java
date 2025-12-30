package com.fredrik.matladan.recipe.model;

import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "recipes")
public class RecipeEntity {
    @Setter (AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;


    //? Normaly String has a limit of 255 but I wanna be able to add more
    //? So we use TEXT instead
    //? Then we can add more than 255 characters to the instructions
    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageURL;

    @Column(nullable = false)
    private Integer servings;

    @Column(nullable = false)
    private Integer prepTime;

    @Column(nullable = false)
    private Integer cookTime;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private DietType dietType;


    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    //? This List is used to store all the ingredients that belong to this recipe
    //? This will make it easier to delete the recipe and all the ingredients that belong to it
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    @PrePersist
    void onCreatingUser(){
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = this.createdDate;

    }

    @PreUpdate
    void onUpdatingUser(){
        this.lastModifiedDate = LocalDateTime.now();
    }

}
