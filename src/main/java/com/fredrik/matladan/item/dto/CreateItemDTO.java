package com.fredrik.matladan.item.dto;

import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.enums.UnitAmountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateItemDTO (
        @NotNull
        @Size(min = 1, max = 50, message = "The name of the item needs to be atleast 1 charecter")
        String name,

        @NotNull (message = "You need to specify where the item is stored")
        StorageLocation storageLocation,

        @NotNull (message = "You need to specify how many of this item is in the storage")
        @Positive (message = "The quantity needs to be of a positive number")
        Integer quantity,

        @Positive (message = "The quantity needs to be of a positive number")
        double sizeOfUnit,

        @NotNull (message = "You need to specify the type of the amount" + "Like gram, kilo etc")
        UnitAmountType unitAmountType,

        LocalDate expiryDate
) {
}
