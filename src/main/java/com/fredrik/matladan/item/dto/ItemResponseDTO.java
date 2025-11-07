package com.fredrik.matladan.item.dto;

import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.enums.UnitAmountType;
import java.time.LocalDate;

public record ItemResponseDTO (
        Long id,
        String name,
        StorageLocation storageLocation,
        LocalDate expiryDate,
        LocalDate addedDate,
        Integer quantity,
        double sizeOfUnit,
        UnitAmountType unitAmountType
) {
}
