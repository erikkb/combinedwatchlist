package com.combinedwatchlist.combined_watchlist.watchlist;

import com.combinedwatchlist.combined_watchlist.show.Show;
import com.combinedwatchlist.combined_watchlist.show.ShowNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public List<Watchlist> findAll() {
        return watchlistRepository.findAll();
    }

    public Watchlist findById(String id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if (watchlist.isEmpty()) {
            throw new ShowNotFoundException("Watchlist with id " + id + " not found");
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
