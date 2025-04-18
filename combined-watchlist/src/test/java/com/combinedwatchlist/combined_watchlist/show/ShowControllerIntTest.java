package com.combinedwatchlist.combined_watchlist.show;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class ShowControllerIntTest {

    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @Autowired
    ShowService showService;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("combined_watchlist")
            .withUsername("erik")
            .withPassword("password");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
        showService.deleteAll();
        showService.save(new Show(
                4,
                false,
                null,
                List.of(80, 18),
                List.of("US"),
                "en",
                "The Sopranos",
                "A mob boss tries to balance the demands of his crime family with those of his personal life.",
                9.3,
                null,
                LocalDate.of(1999, 1, 10),
                "The Sopranos",
                9.3,
                12000
        ));
    }

    @Test
    void shouldFindAllShows() {
        List<Show> shows = restClient.get()
                .uri("/api/shows")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Show>>() {});

        assertEquals(1, shows.size());
    }

    @Test
    void shouldFindShowById() {
        Show show = restClient.get()
                .uri("/api/shows/4")
                .retrieve()
                .body(Show.class);

        assertAll(
                () -> assertEquals(4, show.getId()),
                () -> assertEquals(false, show.isAdult()),
                () -> assertNull(show.getBackdropPath()),
                () -> assertEquals(List.of(80, 18), show.getGenreIds()),
                () -> assertEquals(List.of("US"), show.getOriginCountry()),
                () -> assertEquals("en", show.getOriginalLanguage()),
                () -> assertEquals("The Sopranos", show.getOriginalName()),
                () -> assertEquals("A mob boss tries to balance the demands of his crime family with those of his personal life.", show.getOverview()),
                () -> assertEquals(9.3, show.getPopularity()),
                () -> assertNull(show.getPosterPath()),
                () -> assertEquals(LocalDate.of(1999, 1, 10), show.getFirstAirDate()),
                () -> assertEquals("The Sopranos", show.getName()),
                () -> assertEquals(9.3, show.getVoteAverage()),
                () -> assertEquals(12000, show.getVoteCount())
        );
    }

    @Test
    void shouldCreateNewShow() {
        var show = new Show(5, false, null, List.of(80), List.of("US"), "en", "The Wire", "The Baltimore drug scene, as seen through the eyes of drug dealers and law enforcement.", 9.3, null, LocalDate.of(2002, 6, 2), "The Wire", 9.3, 12000);

        ResponseEntity<Void> newShow = restClient.post()
                .uri("/api/shows")
                .body(show)
                .retrieve()
                .toBodilessEntity();

        assertEquals(201, newShow.getStatusCodeValue());
    }

    @Test
    void shouldUpdateExistingShow() {
        Show show = restClient.get().uri("/api/shows/4").retrieve().body(Show.class);

        ResponseEntity<Void> updatedShow = restClient.put()
                .uri("/api/shows/4")
                .body(show)
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, updatedShow.getStatusCodeValue());
    }

    @Test
    void shouldDeleteShow() {
        ResponseEntity<Void> show = restClient.delete()
                .uri("/api/shows/4")
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, show.getStatusCodeValue());
    }
}