package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.PlaceRespDto;
import com.sketchers.tripsketch_back.dto.trip.create.*;
import com.sketchers.tripsketch_back.entity.Accommodation;
import com.sketchers.tripsketch_back.entity.Place;
import com.sketchers.tripsketch_back.entity.PlaceStore;
import com.sketchers.tripsketch_back.entity.Trip;
import com.sketchers.tripsketch_back.exception.TripInsertException;
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
        try {
            // 1. Trip 저장
            Trip trip = tripCreateReqDto.getTrip().toTrip();
            tripMapper.insertTrip(trip);
            int tripId = trip.getTripId();

            // 2. 일반 장소 저장
            for (StoredPlaceReqDto placeDto : tripCreateReqDto.getStoredPlaces()) {
                try {
                    PlaceInfoDto placeInfo = placeDto.getPlace();
                    Place place = tripMapper.findByGooglePlaceId(placeInfo.getGooglePlaceId());
                    if (place == null) {
                        place = placeInfo.toPlace();
                        tripMapper.insertPlace(place);
                    }

                    PlaceStore placeStore = PlaceStore.builder()
                            .tripId(tripId)
                            .placeId(place.getPlaceId())
                            .stayTime(placeDto.getStayTime())
                            .build();
                    tripMapper.insertPlaceStore(placeStore);

                } catch (Exception e) {
                    throw new TripInsertException("[일반 장소 저장 실패] - " + e.getMessage());
                }
            }

            // 3. 숙소 저장
            for (StoredAccommodationReqDto acDto : tripCreateReqDto.getStoredAccommodations()) {
                try {
                    PlaceInfoDto placeInfo = acDto.getPlace();
                    Place place = tripMapper.findByGooglePlaceId(placeInfo.getGooglePlaceId());
                    if (place == null) {
                        place = placeInfo.toPlace();
                        tripMapper.insertPlace(place);
                    }

                    Accommodation accommodation = Accommodation.builder()
                            .tripId(tripId)
                            .placeId(place.getPlaceId())
                            .checkInDate(acDto.getCheckInDate())
                            .checkOutDate(acDto.getCheckOutDate())
                            .build();
                    tripMapper.insertAccommodation(accommodation);

                } catch (Exception e) {
                    throw new TripInsertException("[숙소 저장 실패] - " + e.getMessage());
                }
            }

            return true;

        } catch (Exception e) {
            throw new TripInsertException("[여행 생성 실패] - " + e.getMessage());
        }
    }

}
