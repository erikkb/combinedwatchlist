package com.combinedwatchlist.combined_watchlist.movie;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository {

    private static final Logger log = LoggerFactory.getLogger(MovieRepository.class);
    private final JdbcClient jdbcClient;

    public MovieRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Movie> findAll() {
        return jdbcClient.sql("SELECT * FROM Movie")
                .query(Movie.class)
                .list();
    }

    public Optional<Movie> findById(long id) {
        return jdbcClient.sql("SELECT id,title,description,genre,rating,release_date FROM Movie WHERE id = :id")
                .param("id", id)
                .query(Movie.class)
                .optional();
    }

    public void create(Movie movie) {
        var updated = jdbcClient.sql("INSERT INTO Movie(id,title,description,genre,rating,release_date) values(?,?,?,?,?,?)")
                .params(List.of(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getGenre(), movie.getRating(), movie.getReleaseDate()))
                .update();

        Assert.state(updated == 1, "Failed to create movie" + movie.getTitle());
    }

    public void update(Movie movie, long id) {
        var updated = jdbcClient.sql("update Movie set title = ?, description = ?, genre = ?, rating = ?, release_date = ? where id = ?")
                .params(List.of(movie.getTitle(), movie.getDescription(), movie.getGenre(), movie.getRating(), movie.getReleaseDate(), id))
                .update();

        Assert.state(updated == 1, "Failed to update movie " + movie.getTitle());
    }

    public void delete(long id) {
        var updated = jdbcClient.sql("delete from Movie where id = :id")
                .param("id", id)
                .update();

        Assert.state(updated == 1, "Failed to delete movie with id " + id);
    }

    public int count() {
        return jdbcClient.sql("select * from Movie")
                .query()
                .listOfRows()
                .size();
    }

    public void saveAll(List<Movie> movies) {
        movies.stream().forEach(this::create);
    }

    public List<Movie> findByGenre(String genre) {
        return jdbcClient.sql("select * from Movie where genre = :genre")
                .param("genre", genre)
                .query(Movie.class)
                .list();
    }
}
