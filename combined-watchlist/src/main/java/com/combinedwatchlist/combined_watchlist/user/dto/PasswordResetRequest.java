package com.combinedwatchlist.combined_watchlist.user.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(@NotBlank String email) {}
