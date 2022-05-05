CREATE TABLE IF NOT EXISTS person_role
(
    person_id INTEGER NOT NULL UNIQUE,
    role_id   INTEGER NOT NULL
);
