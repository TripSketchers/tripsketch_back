package com.sketchers.tripsketch_back.dto.trip;

import com.sketchers.tripsketch_back.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDto {
    private int tripId;
    private int userId;
    private String title;
    private String startDate;
    private String endDate;
    private int tripDestinationId;
    private int transportType;
    private String tripDestinationKoName;
    private String img;

    public Trip toTrip() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return Trip.builder()
                .tripId(tripId)
                .userId(userId)
                .title(title)
                .startDate(start)
                .endDate(end)
                .tripDestinationId(tripDestinationId)
                .transportType(transportType)
                .build();
    }
}
