package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.trip.TripDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trip {
    private int tripId;
    private int userId;
    private String title;
    private Date startDate;
    private Date endDate;
    private int tripDestinationId;
    private int transportType;
    private String tripDestinationKoName;

    public TripDto toTripDto() {
        return TripDto.builder()
                .tripId(tripId)
                .userId(userId)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .tripDestinationId(tripDestinationId)
                .transportType(transportType)
                .tripDestinationKoName(tripDestinationKoName)
                .build();
    }
}
