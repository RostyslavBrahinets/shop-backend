CREATE TABLE IF NOT EXISTS person
(
    id              SERIAL PRIMARY KEY,
    first_name      VARCHAR(256) NOT NULL,
    last_name       VARCHAR(256) NOT NULL
);
