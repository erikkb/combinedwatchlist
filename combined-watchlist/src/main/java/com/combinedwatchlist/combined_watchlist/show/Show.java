package com.combinedwatchlist.combined_watchlist.show;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table("show")
public class Show {

    @Id
    private long id;
    private boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    @JsonProperty("origin_country")
    private List<String> originCountry;
    @JsonProperty("original_language")
    private String originalLanguage;
    @JsonProperty("original_name")
    private String originalName;
    private String overview;
    private double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("first_air_date")
    private LocalDate firstAirDate;
    @NotEmpty
    private String name;
    @JsonProperty("vote_average")
    private double voteAverage;
    @JsonProperty("vote_count")
    private int voteCount;
    //providerNames and Logos not as Pair/Tuple to make storing in database easier, can be turned into Pair in service
    @JsonProperty("provider_names")
    private List<String> providerNames = null;
    @JsonProperty("provider_logos")
    private List<String> providerLogos = null;
    @Column("providerinfo_lastupdate")
    @JsonProperty("providerinfo_lastupdate")
    private LocalDateTime providerInfoLastUpdate = null;
    @Version
    private int version;

    public Show() {
    }

    public Show(long id, boolean adult, String backdropPath, List<Integer> genreIds, List<String> originCountry, String originalLanguage, String originalName, String overview, double popularity, String posterPath, LocalDate firstAirDate, String name, double voteAverage, int voteCount) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.originCountry = originCountry;
        this.originalLanguage = originalLanguage;
        this.originalName = originalName;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.firstAirDate = firstAirDate;
        this.name = name;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Show(long id, boolean adult, String backdropPath, List<Integer> genreIds, List<String> originCountry, String originalLanguage, String originalName, String overview, double popularity, String posterPath, LocalDate firstAirDate, String name, double voteAverage, int voteCount, List<String> providerNames, List<String> providerLogos, LocalDateTime providerInfoLastUpdate) {
        this.id = id;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.originCountry = originCountry;
        this.originalLanguage = originalLanguage;
        this.originalName = originalName;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.firstAirDate = firstAirDate;
        this.name = name;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.providerNames = providerNames;
        this.providerLogos = providerLogos;
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

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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

    public LocalDate getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(LocalDate firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getProviderNames() {
        return providerNames;
    }

    public void setProviderNames(List<String> providerNames) {
        this.providerNames = providerNames;
    }

    public void addProviderName(String providerName) {
        this.providerNames.add(providerName);
    }

    public List<String> getProviderLogos() {
        return providerLogos;
    }

    public void setProviderLogos(List<String> providerLogos) {
        this.providerLogos = providerLogos;
    }

    public void addProviderLogo(String providerLogo) {
        this.providerLogos.add(providerLogo);
    }

    public LocalDateTime getProviderInfoLastUpdate() {
        return providerInfoLastUpdate;
    }

    public void setProviderInfoLastUpdate(LocalDateTime providerInfoLastUpdate) {
        this.providerInfoLastUpdate = providerInfoLastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return getId() == show.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", genreIds=" + genreIds +
                ", originCountry=" + originCountry +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalName='" + originalName + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", firstAirDate=" + firstAirDate +
                ", name='" + name + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}