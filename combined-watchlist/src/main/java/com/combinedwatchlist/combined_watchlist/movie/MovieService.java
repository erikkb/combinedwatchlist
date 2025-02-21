package com.combinedwatchlist.combined_watchlist.movie;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //works for now as workaround to the error with updating via save() stating that the id already exists (something with sequence?)
    public void update(Movie movie, long id) {
        if(id==movie.getId()){
            delete(id);
            save(movie);
        }
    }

    public void delete(long id) {
        movieRepository.delete(findById(id));
    }

    List<Pair<String, String>> getProviders(Movie movie) {
        List<Pair<String, String>> providers = new ArrayList<>();
        List<String> providerNames = movie.getProviderNames();
        List<String> providerLogos = movie.getProviderLogos();
        for (int i = 0; i < providerNames.size(); i++) {
            providers.add((Pair.of(providerNames.get(i), providerLogos.get(i))));
        }
        return providers;
    }

    List<String> getGenreNames(Movie movie) {
        List<String> genreNames = new ArrayList<>();
        List<Integer> genreIds = movie.getGenreIds();
        for (int genreId : genreIds) {
            genreNames.add(MovieGenre.fromId(genreId).getName());
        }
        return genreNames;
    }

    public void deleteAll() {
        movieRepository.deleteAll();
    }

//    List<Movie> findByGenre(String genre) {
//        return movieRepository.findByGenre(genre);
//    }
}