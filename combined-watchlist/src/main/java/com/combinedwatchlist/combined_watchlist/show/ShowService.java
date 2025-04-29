package com.combinedwatchlist.combined_watchlist.show;

import com.combinedwatchlist.combined_watchlist.provider.ProvidersPerCountry;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<Show> shows = showRepository.findAll();
        shows.forEach(Show::hydrateProviders);
        return shows;
    }

    public Show findById(long id) {
        Optional<Show> show = showRepository.findById(id);
        if (show.isEmpty()) {
            throw new ShowNotFoundException("Show with id " + id + " not found");
        }
        Show foundShow = show.get();
        foundShow.hydrateProviders();
        return foundShow;
    }

    public void save(Show show) {
        show.dehydrateProviders();
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

    Pair<Map<String, ProvidersPerCountry> , LocalDateTime> searchProviders(long showId) {
        Show show = null;
        try {
            show = findById(showId);
            if (show.getProviderInfoLastUpdate() != null && show.getProviderInfoLastUpdate().isAfter(LocalDateTime.now().minusHours(24))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider info was updated less than 24 hours ago.");
            }
        } catch (ShowNotFoundException e) {
            System.out.println("Show not found in searchProviders(): " + e.getMessage());
        }

        // Fetch new provider info from the API
        Map<String, ProvidersPerCountry> providers = showRestClient.searchProviders(showId);
        return Pair.of(providers, LocalDateTime.now());
    }
}