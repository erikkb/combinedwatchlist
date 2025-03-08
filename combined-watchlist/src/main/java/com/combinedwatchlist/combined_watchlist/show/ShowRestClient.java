package com.combinedwatchlist.combined_watchlist.show;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ShowRestClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ShowRestClient(RestClient.Builder builder, @Value("${TMDB_KEY}") String apiKey, ObjectMapper objectMapper) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3/")
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
        this.objectMapper = objectMapper;
    }

    public List<Show> searchShowsByName(String showName) {
        JsonNode response = restClient.get()
                .uri("search/tv?query={showName}&include_adult=false&language=en-US&page=1", showName)
                .retrieve()
                .body(JsonNode.class);

        JsonNode resultsNode = response.get("results");
        return objectMapper.convertValue(resultsNode, new TypeReference<List<Show>>() {});
    }
}