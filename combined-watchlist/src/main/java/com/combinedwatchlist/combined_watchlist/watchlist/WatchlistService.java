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
//            watchlist.setId(UUID.randomUUID().toString());
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
            // Don't set ID — not needed for session attribute and would cause issues when migrating to DB
//            watchlist.setId(UUID.randomUUID().toString());
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

    public Watchlist findById(long id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if (watchlist.isEmpty()) {
            throw new WatchlistNotFoundException("Watchlist with id " + id + " not found");
        }
        return watchlist.get();
    }

    public void save(Watchlist watchlist) {
        watchlistRepository.save(watchlist);
    }

    public void delete(long id) {watchlistRepository.delete(findById(id));}

    //works for now as workaround to the error with updating via save() stating that the id already exists (something with sequence?)
    public void update(Watchlist watchlist, long id) {
        if(id == watchlist.getId()) {
//            delete(id);
            save(watchlist);
        }
    }

    public void migrateGuestWatchlistToUser(HttpSession session, Long userId) {
        Watchlist guestWatchlist = (Watchlist) session.getAttribute("watchlist");

        if (guestWatchlist == null) {
            throw new IllegalStateException("Expected watchlist in session, but found none.");
        }

        // Update the guest watchlist with the real user ID and save it
        guestWatchlist.setUserId(userId);

        watchlistRepository.save(guestWatchlist);

        session.removeAttribute("watchlist");
    }
}
