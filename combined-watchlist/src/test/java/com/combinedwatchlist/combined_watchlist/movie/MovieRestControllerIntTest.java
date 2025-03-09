package com.combinedwatchlist.combined_watchlist.movie;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
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

/*
Integration test for the MovieController class.
 */
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class MovieRestControllerIntTest {

    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @Autowired
    MovieService movieService;

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
        movieService.deleteAll();
        movieService.save(new Movie(1,
                false,
                null,
                List.of(28),
                "en",
                "The Matrix",
                "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
                8.7,
                null,
                LocalDate.of(1999, 3, 31),
                "The Matrix",
                false,
                8.7,
                10000));
    }

    @Test
    void shouldFindAllMovies() {
        List<Movie> movies = restClient.get()
                .uri("/api/movies")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Movie>>() {});

        assertEquals(1, movies.size());
    }

    @Test
    void shouldFindMovieById() {
        Movie movie = restClient.get()
                .uri("/api/movies/1")
                .retrieve()
                .body(Movie.class);

        assertAll(
                () -> assertEquals(1, movie.getId()),
                () -> assertEquals(false, movie.isAdult()),
                () -> assertNull(movie.getBackdropPath()),
                () -> assertEquals(List.of(28), movie.getGenreIds()),
                () -> assertEquals("en", movie.getOriginalLanguage()),
                () -> assertEquals("The Matrix", movie.getOriginalTitle()),
                () -> assertEquals("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", movie.getOverview()),
                () -> assertEquals(8.7, movie.getPopularity()),
                () -> assertNull(movie.getPosterPath()),
                () -> assertEquals(LocalDate.of(1999, 3, 31), movie.getReleaseDate()),
                () -> assertEquals("The Matrix", movie.getTitle()),
                () -> assertEquals(false, movie.isVideo()),
                () -> assertEquals(8.7, movie.getVoteAverage()),
                () -> assertEquals(10000, movie.getVoteCount()));
    }

    @Test
    void shouldCreateNewMovie() {
        Movie movie = new Movie(11, false, null, List.of(28), "en", "Inception", "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.", 8.8, null, LocalDate.of(2010, 7, 16), "Inception", false, 8.8, 20000);

        ResponseEntity<Void> newMovie = restClient.post()
                .uri("/api/movies")
                .body(movie)
                .retrieve()
                .toBodilessEntity();

        assertEquals(201, newMovie.getStatusCodeValue());
    }

    @Test
    void shouldUpdateExistingMovie() {
        Movie movie = restClient.get().uri("/api/movies/1").retrieve().body(Movie.class);

        ResponseEntity<Void> updatedMovie = restClient.put()
                .uri("/api/movies/1")
                .body(movie)
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, updatedMovie.getStatusCodeValue());
    }

    @Test
    void shouldDeleteMovie() {
        ResponseEntity<Void> movie = restClient.delete()
                .uri("/api/movies/1")
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, movie.getStatusCodeValue());
    }
}