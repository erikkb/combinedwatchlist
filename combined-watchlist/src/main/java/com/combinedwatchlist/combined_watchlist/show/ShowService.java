package com.combinedwatchlist.combined_watchlist.show;

import com.combinedwatchlist.combined_watchlist.movie.Movie;
import com.combinedwatchlist.combined_watchlist.movie.MovieNotFoundException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowRestClient showRestClient;

    public ShowService(ShowRepository showRepository, ShowRestClient showRestClient) {
        this.showRepository = showRepository;
        this.showRestClient = showRestClient;
    }

    public List<Show> findAll() {
        return showRepository.findAll();
    }

    public Show findById(long id) {
        Optional<Show> show = showRepository.findById(id);
        if (show.isEmpty()) {
            throw new ShowNotFoundException("Show with id " + id + " not found");
        }
        return show.get();
    }

    public void save(Show show) {
        showRepository.save(show);
    }

    //works for now as workaround to the error with updating via save() stating that the id already exists (something with sequence?)
    public void update(Show show, long id) {
        if(id == show.getId()) {
            delete(id);
            save(show);
        }
    }

    public void delete(long id) {
        showRepository.delete(findById(id));
    }

    List<Pair<String, String>> getProviders(Show show) {
        List<Pair<String, String>> providers = new ArrayList<>();
        List<String> providerNames = show.getProviderNames();
        List<String> providerLogos = show.getProviderLogos();
        for (int i = 0; i < providerNames.size(); i++) {
            providers.add(Pair.of(providerNames.get(i), providerLogos.get(i)));
        }
        return providers;
    }

    List<String> getGenreNames(Show show) {
        List<String> genreNames = new ArrayList<>();
        List<Integer> genreIds = show.getGenreIds();
        for (int genreId : genreIds) {
            genreNames.add(ShowGenre.fromId(genreId).getName());
        }
        return genreNames;
    }

    public void deleteAll() {
        showRepository.deleteAll();
    }

    List<Show> searchShowsByName(String showName) {
        return showRestClient.searchShowsByName(showName);
    }

    Pair<List<Pair<String, String>>, LocalDateTime> searchProviders(long showId) {
        Show show = null;
        try {
            show = findById(showId);
            if (show.getProviderInfoLastUpdate() != null && show.getProviderInfoLastUpdate().isAfter(LocalDateTime.now().minusHours(24))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider info was updated less than 24 hours ago.");
            }
        } catch (ShowNotFoundException e) {
            // Log the exception or handle it as needed
            System.out.println("Show not found in searchProviders(): " + e.getMessage());
        }

        // Fetch new provider info from the API
        List<Pair<String, String>> providers = showRestClient.searchProviders(showId);
        return Pair.of(providers, LocalDateTime.now());
    }

//    List<Show> findByGenre(String genre) {
//        return showRepository.findByGenre(genre);
//    }
}