package com.combinedwatchlist.combined_watchlist.provider;

public record Provider(
        int providerId,
        String providerName,
        String logoPath,
        int displayPriority
) {}
