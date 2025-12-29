package com.fredrik.matladan.recipe.dto;


import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String instructions;
    private String imageURL;
    private Integer servings;
    private Integer prepTime;
    private Integer cookTime;
    private MealType mealType;
    private DietType dietType;
    private List<RecipeIngredientDTO> ingredients;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
