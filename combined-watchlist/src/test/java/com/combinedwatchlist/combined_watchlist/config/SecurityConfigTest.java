package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.movie.Movie;
import com.combinedwatchlist.combined_watchlist.movie.MovieRepository;
import com.combinedwatchlist.combined_watchlist.movie.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MovieService movieService;

    private final List<Movie> movies = new ArrayList<>();

    @BeforeEach
    void setUp() {
        movies.add(new Movie(
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
                18605,
                List.of("Disney+"),
                List.of("/4nZz9Q6u6FfFqUjW8v6rL1Y6zrE.jpg"),
                LocalDateTime.now()
        ));
        //in memory H2 default is fine for this test, replace with Testcontainers if needed
        movieService.save(movies.getFirst());
    }


    @Test
    void shouldForbidDeleteForGuestUsers() throws Exception {
        mockMvc.perform(delete("/api/movies/862")
                        .with(SecurityMockMvcRequestPostProcessors.user("guestUser").roles("GUEST_USER")))
                .andExpect(status().isForbidden());
    }
}