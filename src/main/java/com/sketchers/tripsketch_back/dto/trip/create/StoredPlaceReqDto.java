package com.sketchers.tripsketch_back.dto.trip.create;

import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoredPlaceReqDto {
    private PlaceInfoDto place;
    private int stayTime;

    public PlaceStore toPlaceStore() {
        return PlaceStore.builder()
                .stayTime(stayTime)
                .build();
    }
}
