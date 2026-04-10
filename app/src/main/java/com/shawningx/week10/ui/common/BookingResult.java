package com.shawningx.week10.ui.common;

public class BookingResult {
    public enum Status {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR
    }

    private final Status status;
    private final String message;

    private BookingResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static BookingResult idle() {
        return new BookingResult(Status.IDLE, null);
    }

    public static BookingResult loading() {
        return new BookingResult(Status.LOADING, null);
    }

    public static BookingResult success() {
        return new BookingResult(Status.SUCCESS, null);
    }

    public static BookingResult error(String message) {
        return new BookingResult(Status.ERROR, message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
