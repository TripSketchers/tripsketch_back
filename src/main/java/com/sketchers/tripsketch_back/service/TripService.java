package com.sketchers.tripsketch_back.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketchers.tripsketch_back.dto.trip.*;
import com.sketchers.tripsketch_back.entity.*;
import com.sketchers.tripsketch_back.exception.TripInsertException;
import com.sketchers.tripsketch_back.repository.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${google.places.api.key}")
    private String googleApiKey;
    @Value("${kakao.rest.api.key}")
    private String kakaoApiKey;

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

        TripDestination tripDestination = tripMapper.findTripDestinationByTdId(trip.getTripDestinationId());

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
                    Place place = tripMapper.findPlaceByPlaceId(ts.getPlaceId());
                    return TripScheduleDto.builder()
                            .tripScheduleId(ts.getTripScheduleId())
                            .tripId(ts.getTripId())
                            .placeId(ts.getPlaceId())
                            .date(ts.getDate())
                            .startTime(ts.getStartTime())
                            .endTime(ts.getEndTime())
                            .stayTime(ts.getStayTime())
                            .travelTime(ts.getTravelTime())
                            .position(ts.getPosition())
                            .isLocked(ts.getIsLocked())
                            .isAccommodation(ts.getIsAccommodation())
                            .place(place != null ? place.toPlaceInfoDto() : null)
                            .build();
                })
                .collect(Collectors.toList());

        // 5. 최종 DTO 조립 및 반환
        return TripPlanRespDto.builder()
                .trip(trip.toTripDto())
                .tripDestination(tripDestination.toTripDestinationDto())
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

    private boolean isKorea(double lat, double lng) {
        return lat >= 33.0 && lat <= 39.0 && lng >= 124.0 && lng <= 132.0;
    }


    public int getTravelTimeWithRoutesAPI(double originLat, double originLng, double destLat, double destLng, String mode) {
        try {
            // JSON Body를 직접 문자열로 구성 (proto3 JSON 규칙 준수)
            if ("DRIVE".equalsIgnoreCase(mode) && isKorea(originLat, originLng)) {
                return getTravelTimeFromKakao(originLat, originLng, destLat, destLng);
            }
            String body = String.format("""
                    {
                      "origin": {
                        "location": {
                          "latLng": {
                            "latitude": %f,
                            "longitude": %f
                          }
                        }
                      },
                      "destination": {
                        "location": {
                          "latLng": {
                            "latitude": %f,
                            "longitude": %f
                          }
                        }
                      },
                      "travelMode": "%s"
                    }
                    """, originLat, originLng, destLat, destLng, mode);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", googleApiKey);
            headers.set("X-Goog-FieldMask", "routes.duration"); // 최소 응답 설정

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://routes.googleapis.com/directions/v2:computeRoutes",
                    request,
                    String.class
            );

            // 응답 파싱
            JsonNode root = objectMapper.readTree(response.getBody());
            String durationStr = root.path("routes").get(0).path("duration").asText(); // "1018s"
            int seconds = Integer.parseInt(durationStr.replace("s", ""));

            if (durationStr.isEmpty()) {
                System.out.println("Duration not found.");
                return -1;
            }

            return seconds; // 초 단위

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int getTravelTimeFromKakao(double originLat, double originLng, double destLat, double destLng) {
        try {
            String url = String.format("https://apis-navi.kakaomobility.com/v1/directions?origin=%f,%f&destination=%f,%f",
                    originLng, originLat, destLng, destLat);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode durationNode = root.path("routes").get(0).path("summary").path("duration");

            if (durationNode.isMissingNode()) return -1;

            return durationNode.asInt();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Transactional
    public boolean saveTripSchedules(int tripId, List<TripScheduleDto> schedules) {
        tripMapper.deleteTripSchedules(tripId);
        List<TripSchedule> tripSchedules = schedules.stream()
                .map(schedule -> schedule.toTripSchedule())
                .collect(Collectors.toList());
        return tripMapper.insertTripSchedules(tripSchedules) > 0;
    }

}
