package com.sketchers.tripsketch_back.exception;

public class TripInsertException extends RuntimeException {
    public TripInsertException(String message) {
        super(message);
    }

    public TripInsertException(String message, Throwable cause) {
        super(message, cause);
    }
}
