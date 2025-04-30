package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.TripDestination;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TripDestinationRespDto {
    private List<TripDestination> tripDestinations;
}
