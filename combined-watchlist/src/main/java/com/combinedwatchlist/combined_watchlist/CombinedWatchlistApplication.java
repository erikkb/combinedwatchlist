package com.combinedwatchlist.combined_watchlist;

import com.combinedwatchlist.combined_watchlist.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class CombinedWatchlistApplication {

	private static final Logger log = LoggerFactory.getLogger(CombinedWatchlistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CombinedWatchlistApplication.class, args);
	}

	//runs after application is started and application context is loaded by Spring
	@Bean
	CommandLineRunner runner() {
		return args -> {
			log.info("CommandLineRunner running");
			Movie movie = new Movie(1, "The Matrix", "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.", "Action", "R", LocalDate.of(1999, 3, 31));
            log.info(movie.toString());
		};
	}
}
