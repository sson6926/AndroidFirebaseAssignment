package com.shawningx.week10.model;

public class Movie {
    private String id;
    private String title;
    private String description;
    private String posterUrl;
    private int duration;

    // Required empty constructor for Firestore.
    public Movie() {
    }

    public Movie(String id, String title, String description, String posterUrl, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public int getDuration() {
        return duration;
    }
}
