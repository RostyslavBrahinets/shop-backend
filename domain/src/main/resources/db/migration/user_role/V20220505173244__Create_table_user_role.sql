CREATE TABLE IF NOT EXISTS user_role
(
    user_id   INTEGER NOT NULL UNIQUE,
    role_id   INTEGER NOT NULL
);
