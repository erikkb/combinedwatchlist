package com.combinedwatchlist.combined_watchlist;

import com.combinedwatchlist.combined_watchlist.movie.Movie;
//import com.combinedwatchlist.combined_watchlist.movie.JdbcClientMovieRepository;
import com.combinedwatchlist.combined_watchlist.movie.MovieRepository;
import com.combinedwatchlist.combined_watchlist.movie.MovieRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CombinedWatchlistApplication {

	private static final Logger log = LoggerFactory.getLogger(CombinedWatchlistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CombinedWatchlistApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner runner(MovieRestClient movieRestClient) {
		return args -> {
			log.info("CommandLineRunner running");
			try {
				List<Movie> movies = movieRestClient.searchMoviesByName("Toy Story");
				System.out.println(movies);
			} catch (NullPointerException e) {
				log.error("Error occurred while searching for movies by name", e);
			}
		};
	}

//	/**
//	 * Pre-load the database with some movies.
//	 * runs after application is started and application context is loaded by Spring
//	 * !!! comment out if movie table already exists in database
//	 */
//	@Bean
//	@Profile("!test")
//	CommandLineRunner runner(MovieRepository movieRepository) {
//		return args -> {
//			log.info("CommandLineRunner running");
//			List<Movie> movies = new ArrayList<>();
//			movies.add(new Movie(1, false, null, List.of(28), "en", "The Matrix", "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", 8.7, null, LocalDate.of(1999, 3, 31), "The Matrix", false, 8.7, 10000));
//			movies.add(new Movie(2, false, null, List.of(878), "en", "Inception", "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.", 8.8, null, LocalDate.of(2010, 7, 16), "Inception", false, 8.8, 20000));
//			movies.add(new Movie(3, false, null, List.of(878), "en", "Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", 8.6, null, LocalDate.of(2014, 11, 7), "Interstellar", false, 8.6, 15000));
//			movies.add(new Movie(4, false, null, List.of(28), "en", "The Dark Knight", "When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.", 9.0, null, LocalDate.of(2008, 7, 18), "The Dark Knight", false, 9.0, 25000));
//			movies.add(new Movie(5, false, null, List.of(18), "en", "Fight Club", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.", 8.8, null, LocalDate.of(1999, 10, 15), "Fight Club", false, 8.8, 12000));
//			movies.add(new Movie(6, false, null, List.of(80), "en", "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", 8.9, null, LocalDate.of(1994, 10, 14), "Pulp Fiction", false, 8.9, 18000));
//			movies.add(new Movie(
//					862,
//					false,
//					"/3Rfvhy1Nl6sSGJwyjb0QiZzZYlB.jpg",
//					List.of(16, 12, 10751, 35),
//					"en",
//					"Toy Story",
//					"Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.",
//					99.248,
//					"/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg",
//					LocalDate.of(1995, 11, 22),
//					"Toy Story",
//					false,
//					7.97,
//					18605,
//					List.of("Disney+"),
//					List.of("/4nZz9Q6u6FfFqUjW8v6rL1Y6zrE.jpg")
//			));
//			movieRepository.saveAll(movies);
//		};
//	}
}
