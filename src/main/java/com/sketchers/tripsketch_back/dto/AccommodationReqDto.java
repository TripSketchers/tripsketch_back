package com.sketchers.tripsketch_back.dto;

import com.sketchers.tripsketch_back.entity.Accommodation;
import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import lombok.Data;

import java.util.Date;

@Data
public class AccommodationReqDto {
    private String googlePlaceId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String category;
    private double rating;
    private String photoReference;
    private Date checkInDate;
    private Date checkOutDate;

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

    public Accommodation toAccommodation () {
        return Accommodation.builder()
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }
}

