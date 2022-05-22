CREATE TABLE IF NOT EXISTS product
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(256) NOT NULL,
    describe VARCHAR(256) NOT NULL,
    price    REAL         NOT NULL,
    barcode  VARCHAR(256) NOT NULL UNIQUE,
    in_stock BOOL         NOT NULL,
    image    OID
);