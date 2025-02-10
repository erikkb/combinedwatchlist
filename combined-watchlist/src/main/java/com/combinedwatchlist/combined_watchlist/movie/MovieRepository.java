package com.combinedwatchlist.combined_watchlist.movie;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository {

    private List<Movie> movies = new ArrayList<>();

    List<Movie> findAll() {
        return movies;
    }

    Optional<Movie> findById(long id) {
        return movies.stream()
                .filter(movie -> movie.getId() == id)
                .findFirst();
    }

    void create(Movie movie) {
        movies.add(movie);
    }

    void update(Movie movie, long id) {
        Optional<Movie> existingMovie = findById(id);
        existingMovie.ifPresent(mv -> movies.set(movies.indexOf(mv), movie));
    }

    void delete(long id) {
        movies.removeIf(movie -> movie.getId() == id);
    }

    @PostConstruct
    private void init() {
        movies.add(new Movie(1,
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                "Action",
                "R",
                LocalDate.of(1999, 3, 31)));
        movies.add(new Movie(2,
                "Toy Story",
                "A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy",
                "Animation",
                "G",
                LocalDate.of(1995, 11, 22)));
    }
}
