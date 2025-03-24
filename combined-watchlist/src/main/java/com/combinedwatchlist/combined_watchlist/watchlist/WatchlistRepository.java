package com.combinedwatchlist.combined_watchlist.watchlist;

import org.springframework.data.repository.ListCrudRepository;

public interface WatchlistRepository extends ListCrudRepository<Watchlist, String> {
}
