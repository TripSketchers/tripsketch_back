package com.sketchers.tripsketch_back.dto.trip;

import lombok.Data;

import java.util.List;

@Data
public class StoredPlaceUpdateReqDto {
    private List<StoredPlaceDto> storedPlaces;
}
