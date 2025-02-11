package com.combinedwatchlist.combined_watchlist.movie;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends ListCrudRepository<Movie, Long> {

    List<Movie> findByGenre(String genre);
}
