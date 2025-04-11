package com.combinedwatchlist.combined_watchlist.watchlist;

import com.combinedwatchlist.combined_watchlist.user.GuestUser;
import com.combinedwatchlist.combined_watchlist.user.GuestUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final GuestUserService guestUserService;

    public WatchlistService(WatchlistRepository watchlistRepository, GuestUserService guestUserService) {
        this.watchlistRepository = watchlistRepository;
        this.guestUserService = guestUserService;
    }

    public Watchlist getWatchlist(HttpSession session) {
        GuestUser guestUser = guestUserService.getOrCreateGuestUser(session.getId());
        Watchlist watchlist = (Watchlist) session.getAttribute("watchlist");

        if (watchlist == null) {
            watchlist = new Watchlist();
            watchlist.setId(UUID.randomUUID().toString());
            //for future: either make id of actual users (not guest) UUIDs or think of something else (if both are BIGSERIAL there might be a conflict)
            watchlist.setUserId(guestUser.id());
            watchlist.setMovieIds(Collections.emptyList());
            watchlist.setShowIds(Collections.emptyList());
            session.setAttribute("watchlist", watchlist);
        }

        return watchlist;
    }

    public void createWatchlist(HttpSession session) {
        GuestUser guestUser = guestUserService.getOrCreateGuestUser(session.getId());

        if (session.getAttribute("watchlist") == null) {
            Watchlist watchlist = new Watchlist();
            watchlist.setId(UUID.randomUUID().toString());
            watchlist.setUserId(guestUser.id());
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
