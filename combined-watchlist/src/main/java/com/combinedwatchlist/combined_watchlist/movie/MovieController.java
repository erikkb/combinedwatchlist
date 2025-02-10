package com.combinedwatchlist.combined_watchlist.movie;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("")
    List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    Movie findById(@PathVariable long id) {
        Optional<Movie> movie =  movieRepository.findById(id);
        if(movie.isEmpty()) {
            throw new MovieNotFoundException("Movie with id " + id + " not found");
        }
        return movie.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Movie movie) {
        movieRepository.create(movie);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Movie movie, @PathVariable long id) {
        movieRepository.update(movie, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        movieRepository.delete(id);
    }
}
