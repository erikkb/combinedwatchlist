package com.combinedwatchlist.combined_watchlist.movie;

import com.combinedwatchlist.combined_watchlist.show.Show;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    private final MovieRestClient movieRestClient;

    public MovieService(MovieRepository movieRepository, MovieRestClient movieRestClient) {
        this.movieRepository = movieRepository;
        this.movieRestClient = movieRestClient;
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

    List<Movie> searchMoviesByName(String movieName) {
        return movieRestClient.searchMoviesByName(movieName);
    }

    Pair<List<Pair<String, String>>, LocalDateTime> searchProviders(long movieId) {
        Movie movie = null;
        try {
            movie = findById(movieId);
            if (movie.getProviderInfoLastUpdate() != null && movie.getProviderInfoLastUpdate().isAfter(LocalDateTime.now().minusHours(24))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider info was updated less than 24 hours ago.");
            }
        } catch (MovieNotFoundException e) {
            // Log the exception or handle it as needed
            System.out.println("Movie not found in searchProviders(): " + e.getMessage());
        }

        // Fetch new provider info from the API
        List<Pair<String, String>> providers = movieRestClient.searchProviders(movieId);
        return Pair.of(providers, LocalDateTime.now());
    }

//    List<Movie> findByGenre(String genre) {
//        return movieRepository.findByGenre(genre);
//    }
}