package com.combinedwatchlist.combined_watchlist.watchlist;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public Watchlist getWatchlist(HttpSession session) {
        return (Watchlist) session.getAttribute("watchlist");
    }

    public void createWatchlist(HttpSession session) {
        if (session.getAttribute("watchlist") == null) {
            Watchlist watchlist = new Watchlist();
            watchlist.setId(UUID.randomUUID().toString());
            watchlist.setMovieIds(Collections.emptyList());
            watchlist.setShowIds(Collections.emptyList());
            session.setAttribute("watchlist", watchlist);
        }
    }

    public void updateWatchlist(@RequestBody Watchlist watchlist, HttpSession session) {
        session.setAttribute("watchlist", watchlist);
    }

    public Map<String, Object> getSessionAttributes(HttpSession session) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        session.getAttributeNames().asIterator().forEachRemaining(name -> sessionAttributes.put(name, session.getAttribute(name)));
        return sessionAttributes;
    }

    public Watchlist findByUserId(long userId) {
        Optional<Watchlist> watchlist = watchlistRepository.findByUserId(userId);
        if (watchlist.isEmpty()) {
            throw new WatchlistNotFoundException("Watchlist with id " + userId + " not found");
        }
        return watchlist.get();
    }

    public Watchlist findById(String id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if (watchlist.isEmpty()) {
            throw new WatchlistNotFoundException("Watchlist with id " + id + " not found");
        }
        return watchlist.get();
    }

    public void save(Watchlist watchlist) {
        watchlistRepository.save(watchlist);
    }

    public void delete(String id) {watchlistRepository.delete(findById(id));}

    //works for now as workaround to the error with updating via save() stating that the id already exists (something with sequence?)
    public void update(Watchlist watchlist, String id) {
        if(id.equals(watchlist.getId())) {
            delete(id);
            save(watchlist);
        }
    }
}
