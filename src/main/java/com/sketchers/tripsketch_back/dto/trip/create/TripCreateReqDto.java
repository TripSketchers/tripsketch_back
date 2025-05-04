package com.sketchers.tripsketch_back.dto.trip.create;

import lombok.Data;

import java.util.List;

@Data
public class TripCreateReqDto {
    private TripReqDto trip;
    private List<StoredPlaceReqDto> storedPlaces;
    private List<StoredAccommodationReqDto> storedAccommodations;
}
