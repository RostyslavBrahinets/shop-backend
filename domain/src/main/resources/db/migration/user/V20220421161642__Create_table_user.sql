CREATE TABLE IF NOT EXISTS "user"
(
    id              SERIAL PRIMARY KEY,
    first_name      VARCHAR(256) NOT NULL,
    last_name       VARCHAR(256) NOT NULL,
    email           VARCHAR(256) NOT NULL UNIQUE,
    phone           VARCHAR(32)  NOT NULL UNIQUE,
    password        VARCHAR(256) NOT NULL,
    admin_number_id INTEGER UNIQUE,
    CONSTRAINT fk_admin_number
        FOREIGN KEY (admin_number_id)
            REFERENCES admin_number (id)
            ON DELETE CASCADE
);
