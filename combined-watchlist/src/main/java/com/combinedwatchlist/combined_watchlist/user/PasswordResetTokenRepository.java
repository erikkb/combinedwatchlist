package com.combinedwatchlist.combined_watchlist.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends ListCrudRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUserId(Long userId);
}
