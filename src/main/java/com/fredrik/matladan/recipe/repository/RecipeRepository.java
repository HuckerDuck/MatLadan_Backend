package com.fredrik.matladan.recipe.repository;

import com.fredrik.matladan.recipe.enums.DietType;
import com.fredrik.matladan.recipe.enums.MealType;
import com.fredrik.matladan.recipe.model.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    //? Search by name
    List<RecipeEntity> findByNameContaining(String name);
    Page<RecipeEntity> findByNameContaining(String name, Pageable pageable);

    //? Filter by meal type
    List<RecipeEntity> findByMealType(MealType mealType);
    Page<RecipeEntity> findByMealType(MealType mealType, Pageable pageable);

    //? Filter by diet type
    List<RecipeEntity> findByDietType(DietType dietType);
    Page<RecipeEntity> findByDietType(DietType dietType, Pageable pageable);

    //? Combined filters
    List<RecipeEntity> findByMealTypeAndDietType(MealType mealType, DietType dietType);

    //? Adding pagenation for the combined filters
    //? It will make the request from the frontend faster
    //? and save on the server resources

    Page<RecipeEntity> findByMealTypeAndDietType(MealType mealType, DietType dietType, Pageable pageable);
}
