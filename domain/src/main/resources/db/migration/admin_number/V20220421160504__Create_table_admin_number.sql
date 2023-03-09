CREATE TABLE IF NOT EXISTS admin_number
(
    id      SERIAL PRIMARY KEY,
    number  CHAR(8) NOT NULL UNIQUE
);
