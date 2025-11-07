package com.fredrik.matladan.item.mapper;

import com.fredrik.matladan.item.dto.CreateItemDTO;
import com.fredrik.matladan.item.dto.ItemResponseDTO;
import com.fredrik.matladan.item.dto.UpdateItemDTO;
import com.fredrik.matladan.item.entity.Item;
import com.fredrik.matladan.user.model.CustomUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    //? From DTO to Entity
    //? ID is ignored since it's set by the database
    //? Added date is ignored since it's already set by the @PrePersist in the entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "storageOwner", ignore = true)
    Item toEntity(CreateItemDTO createItemDTO, @Context CustomUser customUser);

    //? Entity to ResponseDTO
    ItemResponseDTO toResponseDTO(Item item);

    //? For the Patch method later
    //? NullValuePropertyMappingStrategy.IGNORE is used to say
    //? If the DTO has a null value for a property, then it should be ignored
    //? It's used for the patch method to avoid setting a null value for a field
    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch (UpdateItemDTO updateItemDTO, @MappingTarget Item item);

    //? Below this is run after the MapStruct has done it's automatic mapping
    //? So this is AfterMapping


    //? This is used so that the login in user is connected to the items
    //? That are put into the database

    @AfterMapping
    default void setStorageOwner(@MappingTarget Item item, @Context CustomUser customUser){
        item.setStorageOwner(customUser);
    }


}
