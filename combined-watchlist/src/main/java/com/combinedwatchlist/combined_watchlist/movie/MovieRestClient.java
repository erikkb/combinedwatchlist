package com.combinedwatchlist.combined_watchlist.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MovieRestClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public MovieRestClient(RestClient.Builder builder, @Value("${TMDB_KEY}") String apiKey, ObjectMapper objectMapper) {
        this.restClient = builder
                .baseUrl("https://api.themoviedb.org/3/")
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
        this.objectMapper = objectMapper;
    }

    public List<Movie> searchMoviesByName(String movieName) {
        JsonNode response = restClient.get()
                .uri("search/movie?query={movieName}&include_adult=false&language=en-US&page=1", movieName)
                .retrieve()
                .body(JsonNode.class);

        JsonNode resultsNode = response.get("results");
        return objectMapper.convertValue(resultsNode, new TypeReference<List<Movie>>() {});
    }
}
