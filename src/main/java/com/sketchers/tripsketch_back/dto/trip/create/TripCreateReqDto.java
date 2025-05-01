package com.sketchers.tripsketch_back.dto.trip.create;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import lombok.Data;

import java.util.List;

@Data
public class TripCreateReqDto {
    private TripReqDto trip;
    private List<PlaceRespDto.textSearchInfo> storedPlaces;
    private List<PlaceRespDto.textSearchInfo> storedAccommodations;
}
