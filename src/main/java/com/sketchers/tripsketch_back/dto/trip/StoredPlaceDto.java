package com.sketchers.tripsketch_back.dto.trip;

import com.sketchers.tripsketch_back.entity.PlaceStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredPlaceDto {
    private PlaceInfoDto place;
}
