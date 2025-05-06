package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TripDestination {
    private int tripDestinationId;
    private String enName;
    private String koName;
    private double lowLat;
    private double lowLng;
    private double highLat;
    private double highLng;
    private String img;
}
