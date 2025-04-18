package com.combinedwatchlist.combined_watchlist.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
Unit tests for the MovieController class.
 */
@ActiveProfiles("test")
@WebMvcTest(MovieController.class)
class MovieControllerTest {

	@Configuration
	static class TestSecurityConfig {
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			http.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
			return http.build();
		}
	}

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

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
    }

    @Test
    void shouldFindAll() throws Exception {
        when(movieService.findAll()).thenReturn(movies);
        mvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(movies.size())));
    }

	@Test
	void shouldFindOneMovie() throws Exception {
		Movie movie = movies.getFirst();
		when(movieService.findById(ArgumentMatchers.anyLong())).thenReturn(movie);
		mvc.perform(get("/api/movies/862"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is((int) movie.getId())))
				.andExpect(jsonPath("$.adult", is(movie.isAdult())))
				.andExpect(jsonPath("$.backdrop_path", is(movie.getBackdropPath())))
				.andExpect(jsonPath("$.genre_ids", is(movie.getGenreIds())))
				.andExpect(jsonPath("$.original_language", is(movie.getOriginalLanguage())))
				.andExpect(jsonPath("$.original_title", is(movie.getOriginalTitle())))
				.andExpect(jsonPath("$.overview", is(movie.getOverview())))
				.andExpect(jsonPath("$.popularity", is(movie.getPopularity())))
				.andExpect(jsonPath("$.poster_path", is(movie.getPosterPath())))
				.andExpect(jsonPath("$.release_date", is(movie.getReleaseDate().toString())))
				.andExpect(jsonPath("$.title", is(movie.getTitle())))
				.andExpect(jsonPath("$.video", is(movie.isVideo())))
				.andExpect(jsonPath("$.vote_average", is(movie.getVoteAverage())))
				.andExpect(jsonPath("$.vote_count", is(movie.getVoteCount())));
	}

	@Test
	void shouldReturnNotFoundWithInvalidId() throws Exception {
		mvc.perform(get("/api/runs/-99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldCreateNewMovie() throws Exception {
		var movie = new Movie(6, false, null, List.of(80), "en", "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", 8.9, null, LocalDate.of(1994, 10, 14), "Pulp Fiction", false, 8.9, 18000);
		mvc.perform(post("/api/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(movie))
				)
				.andExpect(status().isCreated());
	}

	@Test
	void shouldUpdateMovie() throws Exception {
		var movie = new Movie(862, false, "/3Rfvhy1Nl6sSGJwyjb0QiZzZYlB.jpg", List.of(16, 12, 10751, 35), "en", "Toy Story Updated", "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.", 99.248, "/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg", LocalDate.of(1995, 11, 22), "Toy Story", false, 7.97, 18605, List.of("Disney+"), List.of("/4nZz9Q6u6FfFqUjW8v6rL1Y6zrE.jpg"), LocalDateTime.now());
		mvc.perform(put("/api/movies/862")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(movie))
				)
				.andExpect(status().isNoContent());
	}

	@Test
	void shouldDeleteMovie() throws Exception {
		mvc.perform(delete("/api/movies/862"))
				.andExpect(status().isNoContent());
	}

}