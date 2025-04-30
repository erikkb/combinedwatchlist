package com.combinedwatchlist.combined_watchlist.show;

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
@RestClientTest(ShowRestClient.class)
class ShowRestClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ShowRestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RateLimitedRequestExecutor rateLimitedRequestExecutor;

    @Test
    void shouldFindShowsByName() throws JsonProcessingException {
        // given
        Show show1 = new Show(1396,
                false,
                "/9faGSFi5jam6pDWGNd0p8JcJgXQ.jpg",
                List.of(18, 80),
                List.of("US"),
                "en",
                "Breaking Bad",
                "Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
                324.178,
                "/ineLOBPG8AZsluYwnkMpHRyu7L.jpg",
                LocalDate.of(2008, 1, 20),
                "Breaking Bad",
                8.9,
                15151);
        List<Show> shows = List.of(show1);

        this.server.expect(requestTo("https://api.themoviedb.org/3/search/tv?query=breaking%20bad&include_adult=false&language=en-US&page=1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(Map.of("results", shows)), MediaType.APPLICATION_JSON));

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
        List<Show> foundShows = client.searchShowsByName("breaking bad");

        // then
        assertEquals(shows.get(0), foundShows.get(0));
    }

}