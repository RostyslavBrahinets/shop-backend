CREATE TABLE IF NOT EXISTS wallet
(
    id                  SERIAL PRIMARY KEY,
    number              VARCHAR NOT NULL UNIQUE,
    amount_of_money     REAL NOT NULL,
    user_id           INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id)
            ON DELETE CASCADE
);
