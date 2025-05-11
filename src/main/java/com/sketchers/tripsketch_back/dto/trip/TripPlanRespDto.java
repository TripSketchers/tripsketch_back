package com.sketchers.tripsketch_back.dto.trip;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TripPlanRespDto {
    private TripDto trip;
    private List<PlaceInfoDto> storedPlaces;
    private List<StoredAccommodationDto> storedAccommodations;
    private List<TripScheduleDto> tripSchedules;
}
