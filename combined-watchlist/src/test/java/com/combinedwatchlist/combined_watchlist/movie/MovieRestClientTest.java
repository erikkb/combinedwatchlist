package com.combinedwatchlist.combined_watchlist.movie;

import com.combinedwatchlist.combined_watchlist.externalapi.RateLimitedRequestExecutor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@RestClientTest(MovieRestClient.class)
class MovieRestClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    MovieRestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RateLimitedRequestExecutor rateLimitedRequestExecutor;

    @Test
    void shouldFindMoviesByName() throws JsonProcessingException {
        // given
        Movie movie1 = new Movie(
                862,
                false,
                "/3Rfvhy1Nl6sSGJwyjb0QiZzZYlB.jpg",
                List.of(16, 12, 10751, 35),
                "en",
                "Toy Story",
                "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.",
                99.248,
                "/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg",
                LocalDate.of(1995, 11, 22),
                "Toy Story",
                false,
                7.97,
                18605
        );
         List<Movie> movies = List.of(movie1);


        server.expect(requestTo("https://api.themoviedb.org/3/search/movie?query=toy%20story&include_adult=false&language=en-US&page=1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(Map.of("results", movies)), MediaType.APPLICATION_JSON));

        // mock executor to run supplier normally
        when(rateLimitedRequestExecutor.executeWithRetry(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyInt(),
                org.mockito.ArgumentMatchers.anyLong()
        )).thenAnswer(invocation -> {
            var supplier = (java.util.function.Supplier<?>) invocation.getArgument(0);
            Object result = supplier.get();
            return CompletableFuture.completedFuture(result);
        });

        // when
        List<Movie> foundMovies = client.searchMoviesByName("toy story");

        // then
        assertEquals(movies.get(0), foundMovies.get(0));
    }

}