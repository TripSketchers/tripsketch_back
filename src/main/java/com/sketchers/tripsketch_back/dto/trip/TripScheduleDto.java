package com.sketchers.tripsketch_back.dto.trip;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
@Builder
public class TripScheduleDto {
    private int tripScheduleId;
    private int tripId;
    private int placeStoreId;
    private Date date;
    private Time startTime;
    private Time endTime;
    private int stayTime;
    private int travelTime;
    private int position;
    private int isLocked;
}
