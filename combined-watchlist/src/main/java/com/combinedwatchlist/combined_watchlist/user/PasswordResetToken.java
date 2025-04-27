package com.combinedwatchlist.combined_watchlist.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("password_reset_tokens")
public record PasswordResetToken(@Id Long id, Long userId, String token, @JsonProperty("expires_at") LocalDateTime expiresAt) {
}
