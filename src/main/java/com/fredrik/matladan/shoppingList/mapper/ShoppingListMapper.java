package com.fredrik.matladan.shoppingList.mapper;

import com.fredrik.matladan.shoppingList.dto.CreateShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.PatchShoppingListDTO;
import com.fredrik.matladan.shoppingList.dto.ShoppingListResponseDTO;
import com.fredrik.matladan.shoppingList.entity.ShoppingListEntity;
import com.fredrik.matladan.user.model.CustomUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    //! CreateDTO -> Entity
    //? ID is ignored since it's set by the database
    //? Added date is ignored since it's already set by the @PrePersist in the entity
    //? SourceRecipeId is ignored since it's set by the database
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isPurchased", constant = "false")
    ShoppingListEntity toEntity (CreateShoppingListDTO createShoppingListDTO, @Context CustomUser customUser);

    //! Entity -> ResponseDTO
    ShoppingListResponseDTO toResponseDTO(ShoppingListEntity shoppingListEntity);

    //! Patch, we only update that is not NULL
    //? This bean mapping is used for the patch method
    //? It will ignore any null values that are sent in the DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(PatchShoppingListDTO patchShoppingListDTO, @MappingTarget ShoppingListEntity shoppingListEntity);

    //! Aftermapping, this will link the shopping list to the user
    @AfterMapping
    default void setOwner(@MappingTarget ShoppingListEntity shoppingListEntity, @Context CustomUser customUser){
        shoppingListEntity.setUser(customUser);
    }
}
