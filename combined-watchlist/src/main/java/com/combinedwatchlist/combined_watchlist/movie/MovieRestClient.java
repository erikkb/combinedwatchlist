package com.combinedwatchlist.combined_watchlist.movie;

import com.combinedwatchlist.combined_watchlist.externalapi.RateLimitedRequestExecutor;
import com.combinedwatchlist.combined_watchlist.provider.Provider;
import com.combinedwatchlist.combined_watchlist.provider.ProvidersPerCountry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MovieRestClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final RateLimitedRequestExecutor rateLimiter;

    public MovieRestClient(RestClient.Builder builder, @Value("${TMDB_KEY}") String apiKey, ObjectMapper objectMapper, RateLimitedRequestExecutor rateLimiter) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3/")
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
        this.objectMapper = objectMapper;
        this.rateLimiter = rateLimiter;
    }

    public List<Movie> searchMoviesByName(String movieName) {
        return rateLimiter.executeWithRetry(() -> {
            JsonNode response = restClient.get()
                    .uri("search/movie?query={movieName}&include_adult=false&language=en-US&page=1", movieName)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode resultsNode = response.get("results");
            return objectMapper.convertValue(resultsNode, new TypeReference<List<Movie>>() {});
        }, 10, 10000).join(); //using join() for now, async becomes relevant at 100+ concurrent users
    }

    public Map<String, ProvidersPerCountry> searchProviders(long movieId) {
        return rateLimiter.executeWithRetry(() -> {
            JsonNode response = restClient.get()
                    .uri("movie/{movieId}/watch/providers", movieId)
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

//    public List<Pair<String, String>> searchProviders(long movieId) {
//        return rateLimiter.executeWithRetry(() -> {
//            JsonNode response = restClient.get()
//                    .uri("movie/{movieId}/watch/providers", movieId)
//                    .retrieve()
//                    .body(JsonNode.class);
//
//            JsonNode flatratesNode = null;
//            try {
//                JsonNode resultsNode = response.get("results");
//                JsonNode deNode = resultsNode.get("DE");
//                flatratesNode = deNode.get("flatrate");
//            } catch (NullPointerException e) {
//                return List.of(Pair.of("noProvider", "noProvider"));
//            }
//
//            List<Pair<String, String>> providers = new ArrayList<>();
//            if (flatratesNode != null && flatratesNode.isArray()) {
//                for (JsonNode providerNode : flatratesNode) {
//                    String providerName = providerNode.get("provider_name").asText();
//                    String logoPath = providerNode.get("logo_path").asText();
//                    providers.add(Pair.of(providerName, logoPath));
//                }
//            } else {
//                return List.of(Pair.of("noProvider", "noProvider"));
//            }
//            return providers;
//        }, 10, 10000).join(); //using join() for now, async becomes relevant at 100+ concurrent users
//    }
}
