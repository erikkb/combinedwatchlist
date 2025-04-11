package com.combinedwatchlist.combined_watchlist.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface GuestUserRepository extends ListCrudRepository<GuestUser, Long> {
    Optional<GuestUser> findBySessionId(String sessionId);
}
