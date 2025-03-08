package com.combinedwatchlist.combined_watchlist.show;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
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

//    List<Show> findByGenre(String genre) {
//        return showRepository.findByGenre(genre);
//    }
}