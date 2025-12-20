package com.fredrik.matladan.recipe.model;

import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "recipes")
public class RecipeEntity {
    @Setter (AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String instructions;

    @Column(nullable = false)
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

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
    @PrePersist
    void onCreatingUser(){
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = this.createdDate;

    }

}
