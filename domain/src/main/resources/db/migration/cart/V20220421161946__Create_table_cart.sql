CREATE TABLE IF NOT EXISTS cart
(
    id              SERIAL PRIMARY KEY,
    total_cost      REAL NOT NULL,
    person_id       INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_person
        FOREIGN KEY (person_id)
            REFERENCES person (id)
            ON DELETE CASCADE
);
