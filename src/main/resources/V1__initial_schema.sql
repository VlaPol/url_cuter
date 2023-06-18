CREATE DATABASE url_cuter;

CREATE TABLE url_synonims
(
    short_name VARCHAR PRIMARY KEY,
    url        VARCHAR not null
);

SELECT us.url, us.short_name
FROM url_synonims us
WHERE us.short_name =:sName;

INSERT INTO url_synonims (short_name, url)
VALUES (:sName, :url);

SELECT us.url
FROM url_synonims us
WHERE us.short_name = :sName