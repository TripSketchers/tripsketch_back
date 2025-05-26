package com.sketchers.tripsketch_back.repository;

import com.sketchers.tripsketch_back.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripMapper {
    public int insertTrip(Trip trip);
    public int updateTrip(Trip trip);
    public Place findByGooglePlaceId(String googlePlaceId);
    public int insertPlace(Place place);
    public int insertPlaceStore(PlaceStore placeStore);
    public int insertAccommodation(Accommodation accommodation);
    public Trip findTripByTripId(int tripId);
    public List<PlaceStore> findStoredPlaceByTripId(int tripId);
    public List<Accommodation> findStoredAccByTripId(int tripId);
    public List<TripSchedule> findTripScheduleByTripId(int tripId);
    public Place findPlaceByPlaceId(int placeId);
    public Place findPlaceByPlaceStoreId(int placeStoreId);
    public int deletePlaceStoresByTripId(int tripId);
    public int deleteAccommodationsByTripId(int tripId);
    public TripDestination findTripDestinationByTdId(int tripDestinationId);
}
