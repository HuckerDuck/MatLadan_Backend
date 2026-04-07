package com.fredrik.matladan.item.dto;

import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.enums.UnitAmountType;

import java.time.LocalDate;

public record UpdateItemDTO(
        String name,
        StorageLocation storageLocation,
        LocalDate expiryDate,
        Integer quantity,
        Double sizeOfUnit,
        UnitAmountType unitAmountType
) {


}
