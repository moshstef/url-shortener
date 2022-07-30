CREATE SCHEMA shorten;

SET
search_path TO shorten;

CREATE SEQUENCE seed minvalue 1000;

CREATE TABLE urls
(
    short_hash    varchar(10) PRIMARY KEY,
    real_url      varchar(300) NOT NULL,
    api_key       varchar(50)  NOT NULL,
    creation_date timestamp    NOT NULL default now()
);

CREATE TABLE users
(
    api_key    varchar(50) PRIMARY KEY,
    urls_count INTEGER NOT NULL DEFAULT 0
);

INSERT INTO users (api_key)
VALUES (12345678);