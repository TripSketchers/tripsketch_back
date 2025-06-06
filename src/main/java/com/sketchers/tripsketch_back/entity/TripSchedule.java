package com.sketchers.tripsketch_back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sketchers.tripsketch_back.dto.trip.TripScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripSchedule {
    private int tripScheduleId;
    private int tripId;
    private int placeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private int stayTime;
    private int travelTime;
    private int position;
    private int isLocked;
    private int isAccommodation;

    public TripScheduleDto toTripScheduleDto() {
        return TripScheduleDto.builder()
                .tripScheduleId(tripScheduleId)
                .tripId(tripId)
                .placeId(placeId)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .stayTime(stayTime)
                .travelTime(travelTime)
                .position(position)
                .isLocked(isLocked)
                .isAccommodation(isAccommodation)
                .build();
    }
}
