CREATE TYPE user_type AS ENUM ('STANDARD', 'MERCHANT');

CREATE CAST (CHARACTER VARYING AS user_type) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS users
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    country_specific_id VARCHAR(255) NOT NULL UNIQUE,
    full_name           VARCHAR(255) NOT NULL,
    email               VARCHAR(128) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    type                user_type    NOT NULL,
    balance             NUMERIC      NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT now(),
    modified_at         TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS transfers
(
    id         VARCHAR(26) NOT NULL UNIQUE PRIMARY KEY,
    payee_id   BIGINT      NOT NULL REFERENCES users (id),
    payer_id   BIGINT      NOT NULL REFERENCES users (id),
    amount     NUMERIC     NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT now()
);