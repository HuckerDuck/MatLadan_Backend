CREATE TABLE shopping_list (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    unit_amount_type VARCHAR(50),
    user_id UUID NOT NULL,
    is_purchased BOOLEAN DEFAULT FALSE,
    source_recipe_id BIGINT,
    added_date TIMESTAMP,
    CONSTRAINT fk_shopping_list_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_shopping_list_recipe
        FOREIGN KEY (source_recipe_id)
        REFERENCES recipes(id)
        ON DELETE SET NULL
);

-- Index for faster searching
CREATE INDEX idx_shopping_list_user_id ON shopping_list(user_id);
CREATE INDEX idx_shopping_list_recipe_id ON shopping_list(source_recipe_id);