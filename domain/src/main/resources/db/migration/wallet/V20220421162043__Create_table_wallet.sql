CREATE TABLE IF NOT EXISTS wallet
(
    id                  SERIAL PRIMARY KEY,
    number              CHAR(16) NOT NULL,
    amount_of_money     REAL NOT NULL,
    person_id           INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_person
        FOREIGN KEY (person_id)
            REFERENCES person (id)
            ON DELETE CASCADE
);