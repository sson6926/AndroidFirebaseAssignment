package com.shawningx.week10.model;

public class Ticket {
    private String id;
    private String userId;
    private String showtimeId;
    private String seatNumber;
    private com.google.firebase.Timestamp createdAt;

    // Required empty constructor for Firestore.
    public Ticket() {
    }

    public Ticket(String id, String userId, String showtimeId, String seatNumber) {
        this.id = id;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public com.google.firebase.Timestamp getCreatedAt() {
        return createdAt;
    }
}
