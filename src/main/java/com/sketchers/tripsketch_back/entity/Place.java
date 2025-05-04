package com.sketchers.tripsketch_back.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Place {
    private int placeId;
    private String googlePlaceId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String category;
    private double rating;
    private String photoReference;
}
