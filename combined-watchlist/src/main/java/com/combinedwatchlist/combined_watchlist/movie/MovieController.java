package com.combinedwatchlist.combined_watchlist.movie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Controller class returning views
 */
@Controller
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public String searchMoviesByName(@RequestParam String query, Model model) {
        List<Movie> movies = movieService.searchMoviesByName(query);
        model.addAttribute("movies", movies);
        return "index";
    }

    @GetMapping("/watchlist")
    public String getWatchlist(Model model) {
        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);
        return "moviewatchlist";
    }
}
