package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.trip.*;
import com.sketchers.tripsketch_back.entity.*;
import com.sketchers.tripsketch_back.exception.TripInsertException;
import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            for (StoredPlaceDto placeDto : tripCreateReqDto.getStoredPlaces()) {
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
                            .build();
                    tripMapper.insertPlaceStore(placeStore);

                } catch (Exception e) {
                    throw new TripInsertException("[일반 장소 저장 실패] - " + e.getMessage());
                }
            }

            // 3. 숙소 저장
            for (StoredAccommodationDto acDto : tripCreateReqDto.getStoredAccommodations()) {
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

    public TripPlanRespDto getTripInfo(int tripId) {
        // 1. Trip 기본 정보 조회
        Trip trip = tripMapper.findTripByTripId(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found with id: " + tripId);
        }

        // 2. Stored Places 조회 (Place 정보 포함)
        List<PlaceStore> placeStores = tripMapper.findStoredPlaceByTripId(tripId);
        List<PlaceInfoDto> storedPlaces = placeStores.stream()
                .map(ps -> {
                    Place place = tripMapper.findPlaceByPlaceId(ps.getPlaceId());
                    return place.toPlaceInfoDto(); // ✅ 바로 PlaceInfoDto로 변환
                })
                .collect(Collectors.toList());

        // 3. Stored Accommodations 조회 (Place 정보 포함)
        List<Accommodation> accommodations = tripMapper.findStoredAccByTripId(tripId);
        List<StoredAccommodationDto> storedAccommodations = accommodations.stream()
                .map(acc -> {
                    Place place = tripMapper.findPlaceByPlaceId(acc.getPlaceId());
                    return StoredAccommodationDto.builder()
                            .place(place.toPlaceInfoDto())
                            .checkInDate(acc.getCheckInDate())
                            .checkOutDate(acc.getCheckOutDate())
                            .build();
                })
                .collect(Collectors.toList());

        // 4. Trip Schedules 조회
        List<TripSchedule> tripSchedules = tripMapper.findTripScheduleByTripId(tripId);
        List<TripScheduleDto> tripScheduleDtos = tripSchedules.stream()
            .map(ts -> {
                Place place = tripMapper.findPlaceByPlaceStoreId(ts.getPlaceStoreId());
                return TripScheduleDto.builder()
                    .tripScheduleId(ts.getTripScheduleId())
                    .tripId(ts.getTripId())
                    .placeStoreId(ts.getPlaceStoreId())
                    .date(ts.getDate())
                    .startTime(ts.getStartTime())
                    .endTime(ts.getEndTime())
                    .stayTime(ts.getStayTime())
                    .travelTime(ts.getTravelTime())
                    .position(ts.getPosition())
                    .isLocked(ts.getIsLocked())
                    .place(place != null ? place.toPlaceInfoDto() : null)
                    .build();
            })
            .collect(Collectors.toList());

        // 5. 최종 DTO 조립 및 반환
        return TripPlanRespDto.builder()
                .trip(trip.toTripDto())
                .storedPlaces(storedPlaces)
                .storedAccommodations(storedAccommodations)
                .tripSchedules(tripScheduleDtos)
                .build();
    }

}
