// might not need fields for sessionId if spring session can handle that (maybe similar for userId)

package com.combinedwatchlist.combined_watchlist.watchlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table("watchlist")
public class Watchlist implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @JsonProperty("user_id")
    private long userId;
    @JsonProperty("movie_ids")
    private List<Long> movieIds;
    @JsonProperty("show_ids")
    private List<Long> showIds;

    public Watchlist() {
    }

    public Watchlist(String id, long userId, List<Long> movieIds, List<Long> showIds) {
        this.id = id;
        this.userId = userId;
        this.movieIds = movieIds != null ? movieIds : new ArrayList<>();
        this.showIds = showIds != null ? showIds : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Long> getMovieIds() {
        return movieIds;
    }

    public void addMovieId(long movieId) {
        movieIds.add(movieId);
    }

    public void setMovieIds(List<Long> movieIds) {
        this.movieIds = movieIds;
    }

    public List<Long> getShowIds() {
        return showIds;
    }

    public void addShowId(long showId) {
        showIds.add(showId);
    }

    public void setShowIds(List<Long> showIds) {
        this.showIds = showIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Watchlist watchlist = (Watchlist) o;
        return getId() == watchlist.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getMovieIds(), getShowIds());
    }
}
