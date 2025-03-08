package com.combinedwatchlist.combined_watchlist.show;

public enum ShowGenre {
    ACTION_ADVENTURE(10759, "Action & Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    KIDS(10762, "Kids"),
    MYSTERY(9648, "Mystery"),
    NEWS(10763, "News"),
    REALITY(10764, "Reality"),
    SCIFI_FANTASY(10765, "Sci-Fi & Fantasy"),
    SOAP(10766, "Soap"),
    TALK(10767, "Talk"),
    WAR_POLITICS(10768, "War & Politics"),
    WESTERN(37, "Western");

    private final int id;
    private final String name;

    ShowGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ShowGenre fromId(int id) {
        for (ShowGenre genre : values()) {
            if (genre.getId() == id) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre id: " + id);
    }

    public static ShowGenre fromName(String name) {
        for (ShowGenre genre : values()) {
            if (genre.getName().equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre name: " + name);
    }
}