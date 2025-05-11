package com.sketchers.tripsketch_back.entity;

import com.sketchers.tripsketch_back.dto.trip.TripScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripSchedule {
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

    public TripScheduleDto toTripScheduleDto() {
        return TripScheduleDto.builder()
                .tripScheduleId(tripScheduleId)
                .tripId(tripId)
                .placeStoreId(placeStoreId)
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
