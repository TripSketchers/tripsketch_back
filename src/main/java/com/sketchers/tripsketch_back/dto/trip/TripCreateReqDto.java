package com.sketchers.tripsketch_back.dto.trip;

import lombok.Data;

import java.util.List;

@Data
public class TripCreateReqDto {
    private TripDto trip;
    private List<StoredPlaceDto> storedPlaces;
    private List<StoredAccommodationDto> storedAccommodations;
}
