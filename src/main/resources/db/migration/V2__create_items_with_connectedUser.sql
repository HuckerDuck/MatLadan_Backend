-- V3__create_items_withconnectedUser.sql
-- Skapar items-tabellen och kopplar till users via user_id (UUID)

CREATE TABLE IF NOT EXISTS items (
                                     id               BIGSERIAL       PRIMARY KEY,
                                     name             VARCHAR(50)     NOT NULL,
                                     storage_location VARCHAR(32)     NOT NULL,
                                     expiry_date      DATE,
                                     added_date       DATE            NOT NULL DEFAULT CURRENT_DATE,
                                     user_id          UUID            NOT NULL,
                                     quantity         INTEGER         NOT NULL CHECK (quantity > 0),
                                     size_of_unit     DOUBLE PRECISION NOT NULL CHECK (size_of_unit > 0),
                                     unit_amount_type      VARCHAR(32)     NOT NULL
);

-- Kopplar items.user_id -> users.id (UUID)
ALTER TABLE items
    ADD CONSTRAINT fk_items_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT;

-- Index för snabb hämtning av en användares items
CREATE INDEX IF NOT EXISTS idx_items_user_id ON items (user_id);

-- Vi ändrar så utifrån regeln som tidigare att det som är i kylen behöver ett utgångsdatum
ALTER TABLE items
    ADD CONSTRAINT chk_items_fridge_requires_expiry
        CHECK (storage_location <> 'FRIDGE' OR expiry_date IS NOT NULL);


CREATE EXTENSION IF NOT EXISTS pgcrypto;