package com.sketchers.tripsketch_back.exception;

public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(int tripId) {
        super("해당 ID의 여행(" + tripId + ")을 찾을 수 없습니다.");
    }
}
