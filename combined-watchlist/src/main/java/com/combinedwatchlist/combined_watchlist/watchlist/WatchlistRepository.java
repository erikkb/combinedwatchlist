package com.combinedwatchlist.combined_watchlist.watchlist;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface WatchlistRepository extends ListCrudRepository<Watchlist, String> {

    Optional<Watchlist> findByUserId(long userId);

}
