package com.sketchers.tripsketch_back.dto.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class TripRespDto {
    private int tripId;
    private String title;
    private String startDate;
    private String endDate;
    private int tripDestinationId;
    private int transportType;
    private String tripDestinationKoName;
    private String img;
}
