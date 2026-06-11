CREATE TABLE household_invites (
                                   id             BIGSERIAL PRIMARY KEY,
                                   household_id   UUID NOT NULL,
                                   invited_email  VARCHAR(160) NOT NULL,
                                   token          VARCHAR(255) NOT NULL UNIQUE,
                                   expiry_date    TIMESTAMP NOT NULL,
                                   created_at     TIMESTAMP NOT NULL DEFAULT NOW(),

                                   CONSTRAINT fk_invite_household
                                       FOREIGN KEY (household_id) REFERENCES households(id) ON DELETE CASCADE
);

CREATE INDEX idx_household_invites_token ON household_invites(token);