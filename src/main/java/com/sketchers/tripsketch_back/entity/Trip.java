package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.main.TripRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
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
    private String img;

    public TripRespDto toMainTripRespDto() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return TripRespDto.builder()
                .tripId(tripId)
                .title(title)
                .startDate(sdf.format(startDate))
                .endDate(sdf.format(endDate))
                .tripDestinationId(tripDestinationId)
                .transportType(transportType)
                .tripDestinationKoName(tripDestinationKoName)
                .img(img)
                .build();
    }
}
