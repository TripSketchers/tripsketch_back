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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripMapper tripMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${GOOGLE_PLACES_API_KEY}")
    private String googleApiKey;
    @Value("${KAKAO_REST_API_KEY}")
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
            tripMapper.updateTrip(tripCreateReqDto.getTrip().toTrip());

            // 1) String → Date 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(tripCreateReqDto.getTrip().getStartDate());
            Date endDate   = sdf.parse(tripCreateReqDto.getTrip().getEndDate());

            // 혹시 start > end일 경우 swap (방어 코드)
            if (startDate.after(endDate)) {
                Date tmp = startDate;
                startDate = endDate;
                endDate = tmp;
            }

            // 2) 일정 중 기간 밖 데이터 삭제
            tripMapper.deleteOutOfRangeSchedulesByTripId(tripId, startDate, endDate);

            // 3) 기존 데이터 삭제 후 재삽입 (완전 교체 방식)
            tripMapper.deletePlaceStoresByTripId(tripId);
            tripMapper.deleteAccommodationsByTripId(tripId);

            savePlaces(tripId, tripCreateReqDto.getStoredPlaces());
            saveAccommodations(tripId, tripCreateReqDto.getStoredAccommodations());

            return true;
        } catch (ParseException pe) {
            throw new TripInsertException("[여행 수정 실패] - 날짜 파싱 오류: " + pe.getMessage(), pe);
        } catch (Exception e) {
            throw new TripInsertException("[여행 수정 실패] - " + e.getMessage(), e);
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

    // 한국(본토+제주+울릉도+독도)만 포함, 쓰시마 제외
    private boolean isKorea(double lat, double lng) {
        boolean kr = (lat >= 33.05 && lat <= 38.7 && lng >= 126.0 && lng <= 129.6)
                || (lat >= 37.30 && lat <= 37.65 && lng >= 130.6 && lng <= 131.2)
                || (lat >= 37.22 && lat <= 37.26 && lng >= 131.85 && lng <= 131.90);
        boolean tsu = (lat >= 34.0 && lat <= 34.9 && lng >= 129.2 && lng <= 129.7);
        return kr && !tsu;
    }

    // DRIVE / TRANSIT만 지원
    public int getTravelTimeWithRoutesAPI(double oLat, double oLng, double dLat, double dLng, String mode) {
        if (Double.compare(oLat, dLat) == 0 && Double.compare(oLng, dLng) == 0) return 0;
        String m = "TRANSIT".equalsIgnoreCase(mode) ? "TRANSIT" : "DRIVE";

        // 한국 내 DRIVE → 카카오 API
        if ("DRIVE".equals(m) && isKorea(oLat, oLng)) {
            return getTravelTimeFromKakao(oLat, oLng, dLat, dLng);
        }

        // Google Routes 호출
        boolean withDeparture = "TRANSIT".equals(m);
        Integer sec = googleRoutesSeconds(oLat, oLng, dLat, dLng, m, withDeparture);
        return sec != null ? sec : -1;
    }

    // Google Routes 호출 (성공 시 초 단위, 실패 시 null)
    private Integer googleRoutesSeconds(double oLat, double oLng, double dLat, double dLng, String mode, boolean withDepartureTime) {
        // TRANSIT이면 departureTime만, DRIVE일 때만 routingPreference 포함
        String dep = withDepartureTime ? String.format(",\"departureTime\":\"%s\"", java.time.Instant.now()) : "";
        String routing = "DRIVE".equalsIgnoreCase(mode) ? ",\"routingPreference\":\"TRAFFIC_AWARE\"" : "";

        String body = String.format("""
                  {
                      "origin":{"location":{"latLng":{"latitude":%f,"longitude":%f}}},
                      "destination":{"location":{"latLng":{"latitude":%f,"longitude":%f}}},
                      "travelMode":"%s","computeAlternativeRoutes":false%s%s
                  }""", oLat, oLng, dLat, dLng, mode, routing, dep);

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("X-Goog-Api-Key", googleApiKey);
        h.set("X-Goog-FieldMask", "routes.duration");

        ResponseEntity<String> r;
        try {
            r = restTemplate.postForEntity(
                    "https://routes.googleapis.com/directions/v2:computeRoutes",
                    new HttpEntity<>(body, h), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            return null;
        }

        if (r == null || !r.getStatusCode().is2xxSuccessful() || r.getBody() == null || r.getBody().isBlank()) return null;

        try {
            com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(r.getBody());
            com.fasterxml.jackson.databind.JsonNode routes = root.path("routes");
            if (!routes.isArray() || routes.size() == 0) return null;

            String dur = routes.at("/0/duration").asText("");
            if (dur.isEmpty()) return null;

            if (dur.endsWith("s")) dur = dur.substring(0, dur.length() - 1);
            double sec = Double.parseDouble(dur);
            return (int) Math.round(sec);
        } catch (Exception e) {
            return null;
        }
    }

    // 한국 DRIVE 전용 (성공 시 초, 실패 시 -1)
    private int getTravelTimeFromKakao(double oLat, double oLng, double dLat, double dLng) {
        try {
            String url = String.format(
                    "https://apis-navi.kakaomobility.com/v1/directions?origin=%f,%f&destination=%f,%f",
                    oLng, oLat, dLng, dLat);

            HttpHeaders h = new HttpHeaders();
            h.set("Authorization", "KakaoAK " + kakaoApiKey);

            ResponseEntity<String> r = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(h), String.class);
            if (r == null || !r.getStatusCode().is2xxSuccessful() || r.getBody() == null) return -1;

            JsonNode root = objectMapper.readTree(r.getBody());
            JsonNode routes = root.path("routes");
            if (!routes.isArray() || routes.size() == 0) return -1;

            JsonNode n = routes.at("/0/summary/duration"); // 초 단위
            return n.isMissingNode() ? -1 : n.asInt();
        } catch (Exception e) {
            return -1;
        }
    }

    @Transactional
    public boolean saveTripSchedules(int tripId, List<TripScheduleDto> schedules) {
        tripMapper.deleteTripSchedules(tripId);

        // ✅ 새로 저장할 일정이 없다면 delete만 하고 true 반환
        if (schedules == null || schedules.isEmpty()) {
            return true;
        }

        List<TripSchedule> tripSchedules = schedules.stream()
                .map(schedule -> schedule.toTripSchedule())
                .collect(Collectors.toList());
        return tripMapper.insertTripSchedules(tripSchedules) > 0;
    }

}
