package com.sketchers.tripsketch_back.dto.trip;

import com.sketchers.tripsketch_back.entity.PlaceStore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoredPlaceDto {
    private PlaceInfoDto place;

    public PlaceStore toPlaceStore() {
        return PlaceStore.builder().build();
    }
}
