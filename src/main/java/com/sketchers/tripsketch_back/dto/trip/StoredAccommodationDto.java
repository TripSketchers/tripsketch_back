package com.sketchers.tripsketch_back.dto.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sketchers.tripsketch_back.entity.Accommodation;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StoredAccommodationDto {
    private PlaceInfoDto place;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date checkOutDate;

    public Accommodation toAccommodation () {
        return Accommodation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }
}

