package com.combinedwatchlist.combined_watchlist.show;

import jakarta.validation.Valid;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("")
    List<Show> findAll() {
        return showService.findAll();
    }

    @GetMapping("/{id}")
    Show findById(@PathVariable long id) {
        return showService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody Show show) {
        showService.save(show);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody Show show, @PathVariable long id) {
        showService.update(show, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {
        showService.delete(id);
    }

    @GetMapping("/search")
    List<Show> searchShowsByName(@RequestParam String showName) {
        return showService.searchShowsByName(showName);
    }

    @GetMapping("/search/providers")
    Pair<List<Pair<String, String>>, LocalDateTime> searchProviders(@RequestParam Long showId) {
        return showService.searchProviders(showId);
    }
}
