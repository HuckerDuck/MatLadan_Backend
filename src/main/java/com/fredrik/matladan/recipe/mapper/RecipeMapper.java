package com.fredrik.matladan.recipe.mapper;

import com.fredrik.matladan.recipe.dto.CreateRecipeDTO;
import com.fredrik.matladan.recipe.dto.RecipeIngredientDTO;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import com.fredrik.matladan.recipe.model.RecipeIngredient;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

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

    //? For the Patch method
    //? NullValuePropertyMappingStrategy.IGNORE is used to say
    //? If the DTO has a null value for a property, then it should be ignored
    //? It's used for the patch method to avoid setting a null value for a field
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void patch(CreateRecipeDTO createRecipeDTO, @MappingTarget RecipeEntity recipeEntity);

    //? Ingredient mapping helpers
    //? MapStruct will use these automatically when mapping nested lists
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    RecipeIngredient toIngredientEntity(RecipeIngredientDTO ingredientDTO);

    RecipeIngredientDTO toIngredientDTO(RecipeIngredient ingredient);

    //? List mapping for ingredients
    List<RecipeIngredient> toIngredientEntityList(List<RecipeIngredientDTO> dtos);
    List<RecipeIngredientDTO> toIngredientDTOList(List<RecipeIngredient> entities);
}