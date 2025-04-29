package com.combinedwatchlist.combined_watchlist.movie;

import com.combinedwatchlist.combined_watchlist.provider.ProvidersPerCountry;
import jakarta.validation.Valid;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
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

    @GetMapping("/search")
    List<Movie> searchMoviesByName(@RequestParam String movieName) {
        return movieService.searchMoviesByName(movieName);
    }

    @GetMapping("/search/providers")
    Pair<Map<String, ProvidersPerCountry>, LocalDateTime> searchProviders(@RequestParam Long movieId) {
        return movieService.searchProviders(movieId);
    }

}
