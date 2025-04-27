package com.combinedwatchlist.combined_watchlist.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public Bucket externalApiRateLimiter() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(35)
                .refillIntervally(35, Duration.ofSeconds(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
