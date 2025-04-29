package com.combinedwatchlist.combined_watchlist.provider;

import java.util.List;

public record ProvidersPerCountry(
        String link,
        List<Provider> flatrate,
        List<Provider> buy,
        List<Provider> rent
) {}
