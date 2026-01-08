CREATE TABLE verification_token (
                                    id BIGSERIAL PRIMARY KEY,
                                    user_id UUID NOT NULL,
                                    token VARCHAR(255) NOT NULL UNIQUE,
                                    creation_date TIMESTAMP NOT NULL,
                                    expiration_date TIMESTAMP NOT NULL,

                                    CONSTRAINT fk_verification_token_user
                                        FOREIGN KEY (user_id)
                                            REFERENCES users(id)
                                            ON DELETE CASCADE
);