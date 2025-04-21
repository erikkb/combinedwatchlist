package com.combinedwatchlist.combined_watchlist.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository  extends ListCrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
