package com.sketchers.tripsketch_back.dto.trip.create;

import com.sketchers.tripsketch_back.entity.Accommodation;
import com.sketchers.tripsketch_back.entity.Place;
import lombok.Data;

import java.util.Date;

@Data
public class StoredAccommodationReqDto {
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

