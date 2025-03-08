package com.combinedwatchlist.combined_watchlist.show;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
Unit tests for the ShowController class.
 */
@ActiveProfiles("test")
@WebMvcTest(ShowController.class)
class ShowControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ShowService showService;

    private final List<Show> shows = new ArrayList<>();

    @BeforeEach
    void setUp() {
        shows.add(new Show(
                1396,
                false,
                "/9faGSFi5jam6pDWGNd0p8JcJgXQ.jpg",
                List.of(18, 80),
                List.of("US"),
                "en",
                "Breaking Bad",
                "Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
                324.178,
                "/ineLOBPG8AZsluYwnkMpHRyu7L.jpg",
                LocalDate.of(2008, 1, 20),
                "Breaking Bad",
                8.9,
                15151,
                List.of("Disney+"),
                List.of("/4nZz9Q6u6FfFqUjW8v6rL1Y6zrE.jpg")
        ));
    }

    @Test
    void shouldFindAll() throws Exception {
        when(showService.findAll()).thenReturn(shows);
        mvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(shows.size())));
    }

    @Test
    void shouldFindOneShow() throws Exception {
        Show show = shows.getFirst();
        when(showService.findById(ArgumentMatchers.anyLong())).thenReturn(show);
        mvc.perform(get("/api/shows/1396"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) show.getId())))
                .andExpect(jsonPath("$.adult", is(show.isAdult())))
                .andExpect(jsonPath("$.backdrop_path", is(show.getBackdropPath())))
                .andExpect(jsonPath("$.genre_ids", is(show.getGenreIds())))
                .andExpect(jsonPath("$.origin_country", is(show.getOriginCountry())))
                .andExpect(jsonPath("$.original_language", is(show.getOriginalLanguage())))
                .andExpect(jsonPath("$.original_name", is(show.getOriginalName())))
                .andExpect(jsonPath("$.overview", is(show.getOverview())))
                .andExpect(jsonPath("$.popularity", is(show.getPopularity())))
                .andExpect(jsonPath("$.poster_path", is(show.getPosterPath())))
                .andExpect(jsonPath("$.first_air_date", is(show.getFirstAirDate().toString())))
                .andExpect(jsonPath("$.name", is(show.getName())))
                .andExpect(jsonPath("$.vote_average", is(show.getVoteAverage())))
                .andExpect(jsonPath("$.vote_count", is(show.getVoteCount())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mvc.perform(get("/api/shows/-99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewShow() throws Exception {
        var show = new Show(
                232533,
                false,
                null,
                List.of(18, 9648),
                List.of("CN"),
                "zh",
                "绝命卦师",
                "During the Ming Dynasty, the emperor sets out for Xinjiang to advocate peace. Shortly after his absence, mysterious occurrences plague the palace and Daoist Tian Mu Tong is called to cast out demons. Together with eunuch Xiao Mo Gu and Jin Yi Wei leader Yun Xiang Rong, Mu Tong uncovers deep dark secrets and conspiracies hidden within conspiracies.",
                10.8,
                "/n8dKkbPkdEPFCFmjNvrUl1gUAYr.jpg",
                LocalDate.of(2016, 4, 11),
                "Breaking Bad Fortune Teller",
                7.5,
                1
        );
        mvc.perform(post("/api/shows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateShow() throws Exception {
        var show = new Show(
                1396,
                false,
                "/9faGSFi5jam6pDWGNd0p8JcJgXQ.jpg",
                List.of(18, 80),
                List.of("US"),
                "en",
                "Updated Breaking Bad",
                "Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
                324.178,
                "/ineLOBPG8AZsluYwnkMpHRyu7L.jpg",
                LocalDate.of(2008, 1, 20),
                "Breaking Bad",
                8.9,
                15151,
                List.of("Disney+"),
                List.of("/4nZz9Q6u6FfFqUjW8v6rL1Y6zrE.jpg")
        );
        mvc.perform(put("/api/shows/1396")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show))
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteShow() throws Exception {
        mvc.perform(delete("/api/shows/1396"))
                .andExpect(status().isNoContent());
    }
}