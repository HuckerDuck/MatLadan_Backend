-- Add recipe_name column to shopping_list table
ALTER TABLE shopping_list
    ADD COLUMN recipe_name VARCHAR(255);