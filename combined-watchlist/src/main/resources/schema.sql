drop table if exists movie;

drop table if exists show;

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
    version INT,
    PRIMARY KEY (id)
);