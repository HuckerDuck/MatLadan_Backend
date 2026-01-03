package com.fredrik.matladan.shoppingList.entity;

import com.fredrik.matladan.item.enums.UnitAmountType;
import com.fredrik.matladan.user.model.CustomUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//? This is the table where the shopping list items will be stored
//? This is the entity that will be used to store the shopping list items
//? It's gonna be based on what items stored in the database
//? But not in the recipe so we can add them to a shopping list
@Entity
@Table(name = "shopping_list")
@Getter
@Setter
public class ShoppingListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit_amount_type")
    private UnitAmountType unitAmountType;

    //? ManyToOne is used to link the ShoppingListEntity to the CustomUser
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    //? Here we have a boolean that we can check mark incase the item is purchased
    //?
    @Column(name = "is_purchased")
    private boolean isPurchased = false;

    //? This is the id of the recipe that the shopping list item is from
    //? This is used to link the shopping list item to the recipe
    @Column(name = "source_recipe_id")

    private Long sourceRecipeId;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    //? This is used to set the added date to the current date if it is null
    @PrePersist
    void onCreate() {
        if (addedDate == null) {
            addedDate = LocalDateTime.now();
        }
    }
}
