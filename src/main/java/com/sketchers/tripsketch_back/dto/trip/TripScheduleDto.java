package com.sketchers.tripsketch_back.dto.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sketchers.tripsketch_back.entity.TripSchedule;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
public class TripScheduleDto {
    private int tripScheduleId;
    private int tripId;
    private int placeId;
    private Date date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private int stayTime;
    private int travelTime;
    private int position;
    private int isLocked;
    private PlaceInfoDto place;

    public TripSchedule toTripSchedule() {
        return TripSchedule.builder()
                .tripId(tripId)
                .placeId(placeId)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .stayTime(stayTime)
                .travelTime(travelTime)
                .position(position)
                .isLocked(isLocked)
                .build();
    }
}
