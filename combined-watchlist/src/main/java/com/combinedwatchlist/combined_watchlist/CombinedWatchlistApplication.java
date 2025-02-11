package com.combinedwatchlist.combined_watchlist;

import com.combinedwatchlist.combined_watchlist.movie.Movie;
//import com.combinedwatchlist.combined_watchlist.movie.JdbcClientMovieRepository;
import com.combinedwatchlist.combined_watchlist.movie.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CombinedWatchlistApplication {

	private static final Logger log = LoggerFactory.getLogger(CombinedWatchlistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CombinedWatchlistApplication.class, args);
	}

	/**
	 * Pre-load the database with some movies.
	 * runs after application is started and application context is loaded by Spring
	 * !!! comment out if movie table already exists in database
	 */

	@Bean
	CommandLineRunner runner(MovieRepository movieRepository) {
		return args -> {
			log.info("CommandLineRunner running");
			List<Movie> movies = new ArrayList<>();
			movies.add(new Movie(1, "The Matrix", "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", "Action", "R", LocalDate.of(1999, 3, 31)));
			movies.add(new Movie(2, "Inception", "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.", "Sci-Fi", "PG-13", LocalDate.of(2010, 7, 16)));
			movies.add(new Movie(3, "Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", "Sci-Fi", "PG-13", LocalDate.of(2014, 11, 7)));
			movies.add(new Movie(4, "The Dark Knight", "When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.", "Action", "PG-13", LocalDate.of(2008, 7, 18)));
			movies.add(new Movie(5, "Fight Club", "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.", "Drama", "R", LocalDate.of(1999, 10, 15)));
			movies.add(new Movie(6, "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", "Crime", "R", LocalDate.of(1994, 10, 14)));
			movieRepository.saveAll(movies);
		};
	}
}
