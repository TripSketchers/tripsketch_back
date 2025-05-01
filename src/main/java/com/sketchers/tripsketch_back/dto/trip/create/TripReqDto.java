package com.sketchers.tripsketch_back.dto.trip.create;

import com.sketchers.tripsketch_back.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripReqDto {
    private int userId;
    private String title;
    private Date startDate;
    private Date endDate;
    private int tripDestinationId;
    private int transportType;

    public Trip toTrip() {
        return Trip.builder()
                .userId(userId)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .tripDestinationId(tripDestinationId)
                .transportType(transportType)
                .build();
    }
}
