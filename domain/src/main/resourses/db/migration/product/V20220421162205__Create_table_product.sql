CREATE TABLE IF NOT EXISTS product
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(256) NOT NULL,
    describe        VARCHAR(256) NOT NULL,
    price           INTEGER NOT NULL,
    category        VARCHAR(32) NOT NULL,
    in_stock        BOOL NOT NULL,
    image           OID
);