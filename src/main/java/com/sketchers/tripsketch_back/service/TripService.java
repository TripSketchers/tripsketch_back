package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.dto.trip.*;
import com.sketchers.tripsketch_back.entity.*;
import com.sketchers.tripsketch_back.exception.TripInsertException;
import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;

    @Transactional
    public boolean insertTrip(TripCreateReqDto tripCreateReqDto) {
        try {
            Trip trip = tripCreateReqDto.getTrip().toTrip();
            tripMapper.insertTrip(trip);
            int tripId = trip.getTripId();

            savePlaces(tripId, tripCreateReqDto.getStoredPlaces());
            saveAccommodations(tripId, tripCreateReqDto.getStoredAccommodations());

            return true;
        } catch (Exception e) {
            throw new TripInsertException("[여행 생성 실패] - " + e.getMessage());
        }
    }

    @Transactional
    public boolean updateTrip(TripCreateReqDto tripCreateReqDto) {
        try {
            int tripId = tripCreateReqDto.getTrip().getTripId();
            System.out.println(tripCreateReqDto.getTrip().toTrip());
            tripMapper.updateTrip(tripCreateReqDto.getTrip().toTrip());

            // 기존 데이터 삭제 (완전 교체 방식)
            tripMapper.deletePlaceStoresByTripId(tripId);
            tripMapper.deleteAccommodationsByTripId(tripId);

            savePlaces(tripId, tripCreateReqDto.getStoredPlaces());
            saveAccommodations(tripId, tripCreateReqDto.getStoredAccommodations());

            return true;
        } catch (Exception e) {
            throw new TripInsertException("[여행 수정 실패] - " + e.getMessage());
        }
    }


    private void savePlaces(int tripId, List<StoredPlaceDto> storedPlaces) {
        for (StoredPlaceDto placeDto : storedPlaces) {
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
        }
    }

    private void saveAccommodations(int tripId, List<StoredAccommodationDto> accommodations) {
        for (StoredAccommodationDto acDto : accommodations) {
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

    @Transactional
    public boolean updateStoredPlaces(int tripId, StoredPlaceUpdateReqDto storedPlaceUpdateReqDto) {
        try {
            // 1. 기존 저장 장소 전부 삭제 (완전 교체 방식)
            tripMapper.deletePlaceStoresByTripId(tripId);

            // 2. 새로 저장할 장소 처리
            for (StoredPlaceDto placeDto : storedPlaceUpdateReqDto.getStoredPlaces()) {
                PlaceInfoDto placeInfo = placeDto.getPlace();

                // 2-1. Place 테이블에 이미 존재하는지 확인
                Place place = tripMapper.findByGooglePlaceId(placeInfo.getGooglePlaceId());
                if (place == null) {
                    // 존재하지 않으면 새로 삽입
                    place = placeInfo.toPlace();
                    tripMapper.insertPlace(place);
                }

                // 2-2. PlaceStore에 새로 매핑 (중복 저장 방지 필요 없으나, 재확인 가능)
                PlaceStore placeStore = PlaceStore.builder()
                        .tripId(tripId)
                        .placeId(place.getPlaceId())
                        .build();

                tripMapper.insertPlaceStore(placeStore);
            }
            return true;
        } catch (Exception e) {
            throw new TripInsertException("[장소 저장 실패] - " + e.getMessage());
        }
    }

}
