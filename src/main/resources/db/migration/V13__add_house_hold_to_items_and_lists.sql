-- Add household_id to items
ALTER TABLE items ADD COLUMN household_id UUID;
ALTER TABLE items ADD CONSTRAINT fk_items_household
    FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE;
CREATE INDEX idx_items_household_id ON items(household_id);

-- Add household_id to shopping_list
ALTER TABLE shopping_list ADD COLUMN household_id UUID;
ALTER TABLE shopping_list ADD CONSTRAINT fk_shopping_list_household
    FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE;
CREATE INDEX idx_shopping_list_household_id ON shopping_list(household_id);

-- Add household_id to recipes
ALTER TABLE recipes ADD COLUMN household_id UUID;
ALTER TABLE recipes ADD CONSTRAINT fk_recipes_household
    FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE;
CREATE INDEX idx_recipes_household_id ON recipes(household_id);