-- V6__update_diet_type_add_fish_chicken.sql
-- Ensures diet_type only allows: MEAT, VEGETARIAN, VEGAN, FISH, CHICKEN
-- Updated now with FISH and CHICKEn

ALTER TABLE recipes
    DROP CONSTRAINT IF EXISTS chk_recipes_diet_type;

ALTER TABLE recipes
    ADD CONSTRAINT chk_recipes_diet_type
        CHECK (diet_type IN ('MEAT', 'VEGETARIAN', 'VEGAN', 'FISH', 'CHICKEN'));


