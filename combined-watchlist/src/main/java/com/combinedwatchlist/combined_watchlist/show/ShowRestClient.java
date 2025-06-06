package com.combinedwatchlist.combined_watchlist.show;

import com.combinedwatchlist.combined_watchlist.externalapi.RateLimitedRequestExecutor;
import com.combinedwatchlist.combined_watchlist.provider.Provider;
import com.combinedwatchlist.combined_watchlist.provider.ProvidersPerCountry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, ProvidersPerCountry> searchProviders(long showId) {
        return rateLimiter.executeWithRetry(() -> {
        JsonNode response = restClient.get()
                .uri("tv/{showId}/watch/providers", showId)
                .retrieve()
                .body(JsonNode.class);

        Map<String, ProvidersPerCountry> providersPerCountryMap = new HashMap<>();

        JsonNode resultsNode = response.path("results");

        resultsNode.fields().forEachRemaining(entry -> {
            String countryCode = entry.getKey();
            JsonNode countryNode = entry.getValue();

            String link = countryNode.path("link").asText();

            List<Provider> flatrate = parseProviders(countryNode.path("flatrate"));
            List<Provider> buy = parseProviders(countryNode.path("buy"));
            List<Provider> rent = parseProviders(countryNode.path("rent"));

            ProvidersPerCountry providers = new ProvidersPerCountry(link, flatrate, buy, rent);
            providersPerCountryMap.put(countryCode, providers);
        });

        return providersPerCountryMap;}, 10, 10000).join(); //using join() for now, async becomes relevant at 100+ concurrent users
    }

    private List<Provider> parseProviders(JsonNode node) {
        if (node == null || node.isMissingNode() || !node.isArray()) {
            return List.of(); // empty list if no providers
        }

        List<Provider> providers = new ArrayList<>();
        for (JsonNode providerNode : node) {
            providers.add(new Provider(
                    providerNode.path("provider_id").asInt(),
                    providerNode.path("provider_name").asText(),
                    providerNode.path("logo_path").asText(),
                    providerNode.path("display_priority").asInt()
            ));
        }
        return providers;
    }
}