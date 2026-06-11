CREATE TABLE households (
                            id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name       VARCHAR(100) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE household_members (
                                   id           BIGSERIAL PRIMARY KEY,
                                   household_id UUID NOT NULL,
                                   user_id      UUID NOT NULL,
                                   role         VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
                                   joined_at    TIMESTAMP NOT NULL DEFAULT NOW(),

                                   CONSTRAINT fk_hm_household FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_hm_user      FOREIGN KEY (user_id)      REFERENCES users(id)      ON DELETE CASCADE,
                                   CONSTRAINT uq_hm_user_household UNIQUE (household_id, user_id)
);

CREATE INDEX idx_household_members_user_id      ON household_members(user_id);
CREATE INDEX idx_household_members_household_id ON household_members(household_id);