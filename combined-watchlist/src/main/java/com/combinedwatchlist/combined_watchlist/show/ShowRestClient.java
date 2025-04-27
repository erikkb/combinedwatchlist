package com.combinedwatchlist.combined_watchlist.show;

import com.combinedwatchlist.combined_watchlist.externalapi.RateLimitedRequestExecutor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowRestClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
     private final RateLimitedRequestExecutor rateLimiter;

    public ShowRestClient(RestClient.Builder builder, @Value("${TMDB_KEY}") String apiKey, ObjectMapper objectMapper, RateLimitedRequestExecutor rateLimiter) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3/")
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
        this.objectMapper = objectMapper;
        this.rateLimiter = rateLimiter;
    }

    public List<Show> searchShowsByName(String showName) {
        return rateLimiter.executeWithRetry(() -> {
            JsonNode response = restClient.get()
                    .uri("search/tv?query={showName}&include_adult=false&language=en-US&page=1", showName)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode resultsNode = response.get("results");
            return objectMapper.convertValue(resultsNode, new TypeReference<List<Show>>() {});
        }, 10, 10000).join(); //using join() for now, async becomes relevant at 100+ concurrent users
    }

    public List<Pair<String, String>> searchProviders(long showId) {
        return rateLimiter.executeWithRetry(() -> {
            JsonNode response = restClient.get()
                    .uri("tv/{showId}/watch/providers", showId)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode flatratesNode = null;
            try {
                JsonNode resultsNode = response.get("results");
                JsonNode deNode = resultsNode.get("DE");
                flatratesNode = deNode.get("flatrate");
            } catch (NullPointerException e) {
                return List.of(Pair.of("noProvider", "noProvider"));
            }

            List<Pair<String, String>> providers = new ArrayList<>();
            if (flatratesNode != null && flatratesNode.isArray()) {
                for (JsonNode providerNode : flatratesNode) {
                    String providerName = providerNode.get("provider_name").asText();
                    String logoPath = providerNode.get("logo_path").asText();
                    providers.add(Pair.of(providerName, logoPath));
                }
            } else {
                return List.of(Pair.of("noProvider", "noProvider"));
            }
            return providers;
        }, 10, 10000).join(); //using join() for now, async becomes relevant at 100+ concurrent users
    }
}