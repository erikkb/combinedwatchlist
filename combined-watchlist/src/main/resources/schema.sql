drop table if exists movie;
drop table if exists show;
drop table if exists watchlist;
drop table if exists guest_users;
drop table if exists users;
drop table if exists password_reset_tokens;
drop table if exists spring_session_attributes;
drop table if exists spring_session;


CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL,
    user_id BIGINT NOT NULL,
    token VARCHAR(36) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS movie (
    id BIGINT NOT NULL,
    adult BOOLEAN NOT NULL,
    backdrop_path VARCHAR(255),
    genre_ids INT[],
    original_language VARCHAR(255),
    original_title VARCHAR(255),
    overview TEXT,
    popularity FLOAT,
    poster_path VARCHAR(255),
    release_date DATE,
    title VARCHAR(255) NOT NULL,
    video BOOLEAN,
    vote_average FLOAT,
    vote_count INT,
    provider_names VARCHAR(255)[],
    provider_logos VARCHAR(255)[],
    providerinfo_lastupdate TIMESTAMP,
    version INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS show (
    id BIGINT NOT NULL,
    adult BOOLEAN NOT NULL,
    backdrop_path VARCHAR(255),
    genre_ids INT[],
    origin_country VARCHAR(255)[],
    original_language VARCHAR(255),
    original_name VARCHAR(255),
    overview TEXT,
    popularity FLOAT,
    poster_path VARCHAR(255),
    first_air_date DATE,
    name VARCHAR(255) NOT NULL,
    vote_average FLOAT,
    vote_count INT,
    provider_names VARCHAR(255)[],
    provider_logos VARCHAR(255)[],
    providerinfo_lastupdate TIMESTAMP,
    version INT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS watchlist (
    id BIGSERIAL NOT NULL,
    user_id BIGINT,
    movie_ids BIGINT[],
    show_ids BIGINT[],
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS guest_users (
    id BIGSERIAL,
    session_id CHAR(36) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX if not exists SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX if not exists SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX if not exists SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);