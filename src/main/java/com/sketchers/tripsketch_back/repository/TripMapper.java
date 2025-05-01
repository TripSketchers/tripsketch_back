package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.Accommodation;
import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import com.sketchers.tripsketch_back.entity.Trip;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TripMapper {
    public int insertTrip(Trip trip);
    public Place findByGooglePlaceId(String googlePlaceId);
    public int insertPlace(Place place);
    public int insertPlaceStore(PlaceStore placeStore);
    public int insertAccommodation(Accommodation accommodation);
}
