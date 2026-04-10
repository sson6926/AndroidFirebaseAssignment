package com.shawningx.week10.model;

public class ShowTime {
    private String id;
    private String movieId;
    private String theaterId;
    private String startTime;

    // Required empty constructor for Firestore.
    public ShowTime() {
    }

    public ShowTime(String id, String movieId, String theaterId, String startTime) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public String getStartTime() {
        return startTime;
    }
}
