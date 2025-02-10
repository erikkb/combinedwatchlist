CREATE TABLE IF NOT EXISTS Movie (
    id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    rating VARCHAR(5)  NOT NULL,
    release_date DATE NOT NULL,
    PRIMARY KEY (id)
);