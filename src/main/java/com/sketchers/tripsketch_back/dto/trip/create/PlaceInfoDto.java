package com.sketchers.tripsketch_back.dto.trip.create;

import com.sketchers.tripsketch_back.entity.Place;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceInfoDto {
    private String googlePlaceId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String category;
    private double rating;
    private String photoReference;

    public Place toPlace() {
        return Place.builder()
                .googlePlaceId(googlePlaceId)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .category(category)
                .rating(rating)
                .photoReference(photoReference)
                .build();
    }
}
