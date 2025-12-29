package com.fredrik.matladan.recipe.mapper;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    //? From DTO to Entity
    //? ID and timestamps are ignored since they're set by the database
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    RecipeEntity toEntity(CreateRecipeDTO createRecipeDTO);

    //? Entity to DTO (response)
    CreateRecipeDTO toDTO(RecipeEntity recipeEntity);
}