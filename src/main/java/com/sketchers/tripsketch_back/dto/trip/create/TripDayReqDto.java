package com.sketchers.tripsketch_back.dto.trip.create;

import lombok.Data;

import java.util.Date;

@Data
public class TripDayReqDto {
    private Date date;
    private String startTime;
}