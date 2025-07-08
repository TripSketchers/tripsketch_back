package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.entity.TripSchedulePlaceView;
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
    private List<TripSchedulePlaceView> tripSchedulePlaceViews;
    private Trip trip;
}
