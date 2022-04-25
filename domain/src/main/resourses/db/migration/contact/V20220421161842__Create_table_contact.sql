CREATE TABLE IF NOT EXISTS contact
(
    id              SERIAL PRIMARY KEY,
    email           VARCHAR(256) UNIQUE,
    phone           VARCHAR(32) UNIQUE,
    person_id       INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_person
        FOREIGN KEY (person_id)
            REFERENCES person (id)
            ON DELETE CASCADE
);