package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import com.sketchers.tripsketch_back.dto.trip.create.*;
import com.sketchers.tripsketch_back.entity.Accommodation;
import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;

    @Transactional
    public boolean insertTrip(TripCreateReqDto tripCreateReqDto) {
        // 1. Trip 저장
        Trip trip = tripCreateReqDto.getTrip().toTrip();  // TripReqDto → Trip
        tripMapper.insertTrip(trip);
        int tripId = trip.getTripId();  // useGeneratedKeys 로 자동 채워짐

        // 2. 일반 장소 저장
        for (PlaceRespDto.textSearchInfo info : tripCreateReqDto.getStoredPlaces()) {
            PlaceInfoDto placeInfo = PlaceRespDto.convertToPlaceInfoDto(info);

            // 2-1. 중복 확인 후 place 저장
            Place place = tripMapper.findByGooglePlaceId(placeInfo.getGooglePlaceId());
            if (place == null) {
                place = placeInfo.toPlace();  // PlaceInfoDto → Place
                tripMapper.insertPlace(place);
            }

            // 2-2. place_store_tb 저장
            PlaceStore placeStore = PlaceStore.builder()
                    .tripId(tripId)
                    .placeId(place.getPlaceId())
                    .stayTime(placeDto.getStayTime())
                    .build();
            tripMapper.insertPlaceStore(placeStore);
        }

        // 3. 숙소 저장
        for (StoredAccommodationReqDto acDto : tripCreateReqDto.getStoredAccommodations()) {
            PlaceInfoDto placeInfo = acDto.getPlace();

            // 3-1. 중복 확인 후 place 저장
            Place place = tripMapper.findByGooglePlaceId(placeInfo.getGooglePlaceId());
            if (place == null) {
                place = placeInfo.toPlace();
                tripMapper.insertPlace(place);
            }

            // 3-2. accommodation_tb 저장
            Accommodation accommodation = Accommodation.builder()
                    .tripId(tripId)
                    .placeId(place.getPlaceId())
                    .checkInDate(acDto.getCheckInDate())
                    .checkOutDate(acDto.getCheckOutDate())
                    .build();
            tripMapper.insertAccommodation(accommodation);
        }
        return true;
    }
}
