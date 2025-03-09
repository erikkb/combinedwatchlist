package com.combinedwatchlist.combined_watchlist.movie;

import jakarta.validation.Valid;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("")
    List<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    Movie findById(@PathVariable long id) {
        return movieService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Movie movie) {
        movieService.save(movie);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Movie movie, @PathVariable long id) {
        movieService.update(movie, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        movieService.delete(id);
    }

    @GetMapping("/search/providers")
    List<Pair<String, String>> searchProviders(@RequestParam Long movieId) {
        return movieService.searchProviders(movieId);
    }

}
