package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TripSchedule {
    private String date;
    private List<TripSchedulePlace> places;
}
