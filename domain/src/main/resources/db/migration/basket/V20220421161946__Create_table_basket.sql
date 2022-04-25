CREATE TABLE IF NOT EXISTS basket
(
    id              SERIAL PRIMARY KEY,
    total_cost      INTEGER NOT NULL,
    person_id       INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_person
        FOREIGN KEY (person_id)
            REFERENCES person (id)
            ON DELETE CASCADE
);