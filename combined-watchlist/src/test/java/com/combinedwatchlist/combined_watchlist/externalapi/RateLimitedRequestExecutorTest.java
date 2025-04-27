package com.combinedwatchlist.combined_watchlist.externalapi;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitedRequestExecutorTest {

    @Test
    void shouldExecuteImmediatelyWhenTokensAvailable() {
        // Given
        Bucket bucket = Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(10)
                        .refillIntervally(10, Duration.ofSeconds(1))
                        .build())
                .build();
        RateLimitedRequestExecutor executor = new RateLimitedRequestExecutor(bucket);

        // When
        CompletableFuture<String> result = executor.executeWithRetry(() -> "success", 3, 3000);

        // Then
        assertEquals("success", result.join());
    }

    @Test
    void shouldRetryAndSucceedAfterTokenBecomesAvailable() throws InterruptedException {
        // Given
        Bucket bucket = Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(1) // 🔥 must be >0 now
                        .refillIntervally(1, Duration.ofMillis(500))
                        .build())
                .build();
        bucket.tryConsume(1); // consume initial token so it starts empty

        RateLimitedRequestExecutor executor = new RateLimitedRequestExecutor(bucket);

        // Use a background thread to manually add tokens after 500ms
        new Thread(() -> {
            try {
                Thread.sleep(600);
                bucket.addTokens(1);
            } catch (InterruptedException ignored) {}
        }).start();

        // When
        CompletableFuture<String> result = executor.executeWithRetry(() -> "success-after-retry", 5, 3000);

        // Then
        assertEquals("success-after-retry", result.join());
    }


    @Test
    void shouldFailAfterMaxRetriesExceeded() {
        // Given
        Bucket bucket = Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(1) // 🔥 must be >0 now
                        .refillIntervally(1, Duration.ofSeconds(10))
                        .build())
                .build();
        bucket.tryConsume(1); // consume initial token so it starts empty

        RateLimitedRequestExecutor executor = new RateLimitedRequestExecutor(bucket);

        // When
        CompletableFuture<String> result = executor.executeWithRetry(() -> "never-happens", 2, 2000);

        // Then
        assertThrows(CompletionException.class, result::join);
    }

}
