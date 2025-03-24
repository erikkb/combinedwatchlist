package com.combinedwatchlist.combined_watchlist.watchlist;

import com.combinedwatchlist.combined_watchlist.show.Show;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.util.Pair;
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
        return (Watchlist) session.getAttribute("watchlist");
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createWatchlist(HttpSession session) {
        if (session.getAttribute("watchlist") == null) {
            Watchlist watchlist = new Watchlist();
            watchlist.setId(UUID.randomUUID().toString());
            watchlist.setMovieIds(Collections.emptyList());
            watchlist.setShowIds(Collections.emptyList());
            session.setAttribute("watchlist", watchlist);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("")
    public void updateWatchlist(@RequestBody Watchlist watchlist, HttpSession session) {
        session.setAttribute("watchlist", watchlist);
    }

    @GetMapping("/session")
    public Map<String, Object> getSessionAttributes(HttpSession session) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        session.getAttributeNames().asIterator().forEachRemaining(name -> sessionAttributes.put(name, session.getAttribute(name)));
        return sessionAttributes;
    }

//    @GetMapping("")
//    List<Watchlist> findAll() { return watchlistService.findAll();}

    @GetMapping("/{id}")
    Watchlist getWatchlistById(@PathVariable String id) { return watchlistService.findById(id);}

//    @GetMapping("/session/{id}")
//    Watchlist getWatchlistBySession(@PathVariable long id) {watchlistService.findBySessionId(id);}
//
//    @GetMapping("/user/{id}")
//    Watchlist getWatchlistbyUser(@PathVariable long id) {watchlistService.findByUserId(id);}

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("")
//    void create(@RequestBody Watchlist watchlist) {watchlistService.save(watchlist);}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody Watchlist watchlist, String id) {watchlistService.update(watchlist, id);}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {watchlistService.delete(id);}
}