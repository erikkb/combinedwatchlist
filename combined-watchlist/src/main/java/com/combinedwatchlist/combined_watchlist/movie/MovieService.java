package com.combinedwatchlist.combined_watchlist.movie;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Movie findById(long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new MovieNotFoundException("Movie with id " + id + " not found");
        }
        return movie.get();
    }

    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    //works for now as workaround to the error with updating via save() stating that the id already exists
    public void update(Movie movie, long id) {
        if(id==movie.getId()){
            delete(id);
            save(movie);
        }
    }

    public void delete(long id) {
        movieRepository.delete(findById(id));
    }

    List<Movie> findByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }
}