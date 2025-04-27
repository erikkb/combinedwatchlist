package com.combinedwatchlist.combined_watchlist.externalapi;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.function.Supplier;

@Component
public class RateLimitedRequestExecutor {

    private final Bucket rateLimiter;
    private final ScheduledExecutorService scheduler;

    public RateLimitedRequestExecutor(Bucket externalApiRateLimiter) {
        this.rateLimiter = externalApiRateLimiter;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    public <T> CompletableFuture<T> executeWithRetry(Supplier<T> action, int maxRetries, long timeoutMs) {
        CompletableFuture<T> future = new CompletableFuture<>();
        long startTime = System.currentTimeMillis();
        attempt(action, future, startTime, 0, maxRetries, timeoutMs);
        return future;
    }

    private <T> void attempt(Supplier<T> action,
                             CompletableFuture<T> future,
                             long startTime,
                             int attempt,
                             int maxRetries,
                             long timeoutMs) {

        if (System.currentTimeMillis() - startTime > timeoutMs) {
            future.completeExceptionally(new RuntimeException("Rate limit timeout exceeded"));
            return;
        }

        if (rateLimiter.tryConsume(1)) {
            try {
                T result = action.get();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        } else {
            if (attempt < maxRetries) {
                scheduler.schedule(() ->
                                attempt(action, future, startTime, attempt + 1, maxRetries, timeoutMs),
                        1000, TimeUnit.MILLISECONDS);
            } else {
                future.completeExceptionally(new RuntimeException("Max retry attempts exceeded"));
            }
        }
    }
}
