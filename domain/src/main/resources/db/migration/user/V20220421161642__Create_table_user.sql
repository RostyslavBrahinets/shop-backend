CREATE TABLE IF NOT EXISTS "user"
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(256) NOT NULL,
    last_name    VARCHAR(256) NOT NULL,
    email        VARCHAR(256) NOT NULL UNIQUE,
    phone        VARCHAR(32)  NOT NULL UNIQUE,
    password     VARCHAR(256) NOT NULL,
    admin_number CHAR(8)
);
