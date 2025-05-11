package com.sketchers.tripsketch_back.dto.trip;

import com.sketchers.tripsketch_back.entity.Accommodation;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StoredAccommodationDto {
    private PlaceInfoDto place;
    private Date checkInDate;
    private Date checkOutDate;

    public Accommodation toAccommodation () {
        return Accommodation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }
}

