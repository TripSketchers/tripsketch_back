package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoredPlaceReqDto {
    private String googlePlaceId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String category;
    private double rating;
    private String photoReference;
    private int stayTime;

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

    public PlaceStore toPlaceStore() {
        return PlaceStore.builder()
                .stayTime(stayTime)
                .build();
    }
}
