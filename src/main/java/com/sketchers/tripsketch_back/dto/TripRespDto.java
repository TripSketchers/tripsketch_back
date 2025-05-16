package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TripRespDto {
    private List<Trip> tripList;
}
