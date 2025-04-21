package com.combinedwatchlist.combined_watchlist.watchlist;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("")
    public Watchlist getWatchlist(HttpSession session) {
        return watchlistService.getWatchlist(session);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createWatchlist(HttpSession session) {
        watchlistService.createWatchlist(session);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("")
    public void updateWatchlist(@RequestBody Watchlist watchlist, HttpSession session) {
        watchlistService.updateWatchlist(watchlist, session);
    }

    @GetMapping("/session")
    public Map<String, Object> getSessionAttributes(HttpSession session) {
        return watchlistService.getSessionAttributes(session);
    }

    @GetMapping("/{id}")
    Watchlist getWatchlistById(@PathVariable long id) { return watchlistService.findById(id);}

    @GetMapping("/user/{id}")
    Watchlist getWatchlistByUserId(@PathVariable long id) { return watchlistService.findByUserId(id);}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Watchlist watchlist,  @PathVariable long id) {watchlistService.update(watchlist, id);}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable long id) {watchlistService.delete(id);}
}