package com.combinedwatchlist.combined_watchlist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class CombinedWatchlistApplication {

	private static final Logger log = LoggerFactory.getLogger(CombinedWatchlistApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CombinedWatchlistApplication.class, args);
	}
}
