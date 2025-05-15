package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.trip.PlaceInfoDto;
import com.sketchers.tripsketch_back.dto.trip.StoredPlaceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceStore {
    private int placeStoreId;
    private int placeId;
    private int tripId;
}
