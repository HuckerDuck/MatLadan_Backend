-- Create the recipes table
CREATE TABLE recipes (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(500) NOT NULL,
                         instructions TEXT NOT NULL,
                         image_url VARCHAR(500) NOT NULL,
                         servings INTEGER NOT NULL,
                         prep_time INTEGER NOT NULL,
                         cook_time INTEGER NOT NULL,
                         meal_type VARCHAR(50) NOT NULL,
                         diet_type VARCHAR(50) NOT NULL,
                         created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create the  recipe_ingredients table

CREATE TABLE recipe_ingredients (
                                    id BIGSERIAL PRIMARY KEY,
                                    recipe_id BIGINT NOT NULL,
                                    ingredient_name VARCHAR(100) NOT NULL,
                                    amount DOUBLE PRECISION NOT NULL,
                                    unit_amount_type VARCHAR(50) NOT NULL,
    -- It connectts it to the recipe table with the connected id
                                    CONSTRAINT fk_recipe FOREIGN KEY (recipe_id)
                                        REFERENCES recipes(id) ON DELETE CASCADE
);

-- Create indexes for better performance
-- This will make it so that we can search for recipes based on the meal type
CREATE INDEX idx_recipe_meal_type ON recipes(meal_type);
CREATE INDEX idx_recipe_diet_type ON recipes(diet_type);

-- Or we can search for recipes based on the name
CREATE INDEX idx_recipe_name ON recipes(name);
CREATE INDEX idx_recipe_ingredients_recipe_id ON recipe_ingredients(recipe_id);