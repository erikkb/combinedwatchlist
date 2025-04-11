package com.combinedwatchlist.combined_watchlist.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("guest_users")
public record GuestUser(@Id Long id, String sessionId) {
}