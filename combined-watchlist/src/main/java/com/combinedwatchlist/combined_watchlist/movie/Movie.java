package com.combinedwatchlist.combined_watchlist.movie;

import com.combinedwatchlist.combined_watchlist.provider.ProvidersPerCountry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import org.postgresql.util.PGobject;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Table("movie")
public class Movie {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    private long id;
    private boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_title")
    private String originalTitle;
    private String overview;
    private double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private LocalDate releaseDate;
    @NotEmpty
    private String title;
    private boolean video;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    @Column("providers")
    private String providersJson = null;
    @Transient
    private Map<String, ProvidersPerCountry> providers = null;
//    //providerNames and Logos not as Pair/Tuple to make storing in database easier, can be turned into Pair in service
//    @JsonProperty("provider_names")
//    private List<String> providerNames = null;
//    @JsonProperty("provider_logos")
//    private List<String> providerLogos = null;
    @Column("providerinfo_lastupdate")
    @JsonProperty("providerinfo_lastupdate")
    private LocalDateTime providerInfoLastUpdate = null;
    @Version
    private int version;


    public Movie() {
    }

    public Movie(long id, boolean adult, String backdropPath, List<Integer> genreIds, String originalLanguage, String originalTitle, String overview, double popularity, String posterPath, LocalDate releaseDate, String title, boolean video, double voteAverage, int voteCount) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Movie(long id, boolean adult, String backdropPath, List<Integer> genreIds, String originalLanguage, String originalTitle, String overview, double popularity, String posterPath, LocalDate releaseDate, String title, boolean video, double voteAverage, int voteCount, Map<String, ProvidersPerCountry> providers, LocalDateTime providerInfoLastUpdate) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.providers = providers;
//        this.providerNames = providerNames;
//        this.providerLogos = providerLogos;
        this.providerInfoLastUpdate = providerInfoLastUpdate;
    }

    public Movie(long id, boolean adult, String backdropPath, List<Integer> genreIds, String originalLanguage, String originalTitle, String overview, double popularity, String posterPath, LocalDate releaseDate, String title, boolean video, double voteAverage, int voteCount, String providersJson, LocalDateTime providerInfoLastUpdate) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.providersJson = providersJson;
        this.providerInfoLastUpdate = providerInfoLastUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

//    public List<String> getProviderNames() {
//        return providerNames;
//    }
//
//    public void setProviderNames(List<String> providerNames) {
//        this.providerNames = providerNames;
//    }
//
//    public void addProviderName(String providerName) {
//        this.providerNames.add(providerName);
//    }
//
//    public List<String> getProviderLogos() {
//        return providerLogos;
//    }
//
//    public void setProviderLogos(List<String> providerLogos) {
//        this.providerLogos = providerLogos;
//    }
//
//    public void addProviderLogo(String providerLogo) {
//        this.providerLogos.add(providerLogo);
//    }

    public Map<String, ProvidersPerCountry> getProviders() {
        return providers;
    }

    public void setProviders(Map<String, ProvidersPerCountry> providers) {
        this.providers = providers;
    }

    public LocalDateTime getProviderInfoLastUpdate() {
        return providerInfoLastUpdate;
    }

    public void setProviderInfoLastUpdate(LocalDateTime providerInfoLastUpdate) {
        this.providerInfoLastUpdate = providerInfoLastUpdate;
    }

    public String getProvidersJson() {
        return providersJson;
    }

    public void setProvidersJson(String providersJson) {
        this.providersJson = providersJson;
    }

    public void hydrateProviders() {
        if (this.providersJson != null) {
            try {
                this.providers = mapper.readValue(
                        this.providersJson,
                        mapper.getTypeFactory().constructMapType(Map.class, String.class, ProvidersPerCountry.class)
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize providers JSON", e);
            }
        }
    }

    public void dehydrateProviders() {
        if (this.providers != null) {
            try {
                this.providersJson = mapper.writeValueAsString(this.providers);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize providers map", e);
            }
        }
    }

//    @Column("providers")
//    public PGobject getProvidersJsonForDb() {
//        if (providersJson == null) {
//            return null;
//        }
//        try {
//            PGobject jsonObject = new PGobject();
//            jsonObject.setType("jsonb");
//            jsonObject.setValue(providersJson);
//            return jsonObject;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create PGobject for providersJson", e);
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return getId() == movie.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", genreIds=" + genreIds +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate=" + releaseDate +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", providersJson='" + providersJson + '\'' +
                ", providers=" + providers +
//                ", providerNames=" + providerNames +
//                ", providerLogos=" + providerLogos +
                ", providerInfoLastUpdate=" + providerInfoLastUpdate +
                '}';
    }
}