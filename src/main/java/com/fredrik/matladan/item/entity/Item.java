package com.fredrik.matladan.item.entity;

import com.fredrik.matladan.item.enums.StorageLocation;
import com.fredrik.matladan.item.enums.UnitAmountType;
import com.fredrik.matladan.user.model.CustomUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
//? This is the table where the items will be stored
@Table (name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //? This is so that no wone can change the id of the item
    @Setter (AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StorageLocation storageLocation;

    //? This can be null for Freezer or Pantry
    //? But this will not be null for Fridge
    //? Will fix in Service later
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "added_date", nullable = false)
    private LocalDate addedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser storageOwner;

    //? How many of this item is in the storage
    @Column(nullable = false)
    private Integer quantity;

    //? How big is the unit of this item
    @Column(name = "size_of_unit", nullable = false)
    private double sizeOfUnit;

    //? This is the description of the item
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_amount_type")
    private UnitAmountType unitAmountType;

    @PrePersist
    void onCreatingAnItem(){
        //? Set the added date to the current date if it is null
        if (addedDate == null){
            addedDate = LocalDate.now();
        }
    }
}
