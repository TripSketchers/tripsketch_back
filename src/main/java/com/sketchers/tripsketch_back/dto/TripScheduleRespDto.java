package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.TripSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripScheduleRespDto {
    private String startDate;
    private List<TripSchedule> tripSchedules;
}
